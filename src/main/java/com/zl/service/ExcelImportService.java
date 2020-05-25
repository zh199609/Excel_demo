package com.zl.service;

import com.zl.annotation.Excel;
import com.zl.excel.ExcelImportEntity;
import com.zl.excel.ImportParams;
import com.zl.excel.ImportResult;
import com.zl.excel.verify.ExcelVerifyHandlerResult;
import com.zl.excel.verify.IExcelVerifyHandler;
import com.zl.util.CellUtil;
import com.zl.util.PoiReflectorUtil;
import com.zl.util.PublicUtils;
import com.zl.util.ValidatorUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 导入服务
 */
@Service
public class ExcelImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportService.class);

    @Autowired
    private CellValueService cellValueService;


    public ImportResult importExcelBase(InputStream inputStream, Class<?> clazz, ImportParams importParams) {
        LOGGER.debug("Excel import start,ClassType is {}", clazz);
        ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
        ImportResult importResult = null;
        try {
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) > -1) {
                arrayOut.write(bytes, 0, len);
            }
            arrayOut.flush();
            //进行克隆流
            InputStream arrayInputStream = new ByteArrayInputStream(arrayOut.toByteArray());
            LOGGER.debug("Excel InputStream Clone Success");
            Workbook workbook = WorkbookFactory.create(arrayInputStream);
            LOGGER.debug("Workbook create Success");
            long startTime = System.currentTimeMillis();
            Sheet sheet = workbook.getSheetAt(importParams.getSheetIndex());
            if (sheet == null) {
                throw new RuntimeException("所对应的Sheet不存在");
            }
            //importResult的list属性存放读取正确的数据
            importResult = importExcel(sheet, importParams, clazz);
            long endTime = System.currentTimeMillis();
            LOGGER.debug("excel读取耗时：" + (endTime - startTime) + "毫秒");
        } catch (Exception e) {
            LOGGER.error("importExcelBase Exception occurred:{}", e.getMessage(), e);
            e.printStackTrace();
        }
        return importResult;
    }


    public ImportResult importExcel(Sheet sheet, ImportParams importParams, Class<?> pojoClass) {
        ImportResult result = new ImportResult<>();
        Field[] classFields = PublicUtils.getClassFields(pojoClass);
        //获取导入字段
        Map<String, ExcelImportEntity> excelParams = getAllExcelField(classFields, pojoClass);
        //导入的数据行数
        int lastRowNum = sheet.getLastRowNum();

        Iterator<Row> rowIterator = sheet.rowIterator();
        //跳过title和head的行
        for (int i = 0; i <= importParams.getTitleRow(); i++) {
            rowIterator.next();
        }
        Map<Integer, String> titleMap = getTitleMap(rowIterator, importParams, excelParams);
        //模板校验错误直接返回
        boolean titleVerify = verifyExcelTemplate(importParams, titleMap, excelParams, result);
        if (!titleVerify) {
            return result;
        }
        //从数据行开始读取  跳过无效的行数
        for (int i = importParams.getHeadRow(); i < importParams.getDataRow() - 1; i++) {
            rowIterator.next();
        }
        //数据开始读取
        Row row;
        List<String> errorMsgList = new ArrayList<>();
        List errorDataList = new ArrayList<>();
        List dataList = new ArrayList<>(lastRowNum);
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            //跳出循环
            if (sheet.getLastRowNum() - row.getRowNum() < 0) {
                break;
            }
            StringBuilder errorMsg = new StringBuilder();
            //数据对象创建
            Object object = PublicUtils.createObject(pojoClass);
            try {
                Set<Integer> titleIndex = titleMap.keySet();
                for (Integer index : titleIndex) {
                    Cell cell = row.getCell(index);
                    String titleName = titleMap.get(index);
                    //给对象的属性赋值
                    setFieldValue(object, cell, excelParams, titleName, errorMsg);
                }
                //数据校验   如类型转化错误将跳过JSR303和自定义的校验器，类型错误会出现null，避免出现重复错误信息
                if (StringUtils.isBlank(errorMsg) && verifyData(object, row, importParams, errorMsg)) {
                    dataList.add(object);
                } else {
                    errorDataList.add(object);
                    String errorNumMsg = "第" + (row.getRowNum() + 1) + "行数据：";
                    errorMsg.deleteCharAt(errorMsg.length() - 1);
                    errorMsgList.add(errorNumMsg + errorMsg);
                }
            } catch (Exception e) {
                LOGGER.error("importExcel Exception occurred:{}", e.getMessage(), e);
            }
        }
        //错误数据返回
        if (CollectionUtils.isNotEmpty(errorMsgList)) {
            result.setVerfiyFail(true);
            result.setVerifyMsg(errorMsgList);
            result.setFailList(errorDataList);
        }
        result.setList(dataList);
        return result;
    }

    /**
     * 功能描述:
     * 〈数据校验〉
     *
     * @param object
     * @param row
     * @param importParams
     * @param errorMsg
     * @return : boolean
     */
    public boolean verifyData(Object object, Row row, ImportParams importParams, StringBuilder errorMsg) {
        //true正确
        boolean verifyResult = true;
        if (importParams.isVerify()) {
            //JSR303验证
            String validationMsg = ValidatorUtil.validation(object);
            if (StringUtils.isNotBlank(validationMsg)) {
                errorMsg.append(validationMsg);
                verifyResult = false;
            }
        }
        //自定义验证
        IExcelVerifyHandler excelVerifyHandler = importParams.getExcelVerifyHandler();
        if (excelVerifyHandler != null) {
            ExcelVerifyHandlerResult verifyHandler = excelVerifyHandler.verifyHandler(object);
            if (!verifyHandler.isSuccess()) {
                errorMsg.append(verifyHandler.getMsg());
                verifyResult = false;
            }
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            verifyResult = false;
        }
        return verifyResult;
    }

    private void setFieldValue(Object object, Cell cell, Map<String, ExcelImportEntity> excelParams, String titleName, StringBuilder errorMsg) throws Exception {
        //获取属性值  包含类型的校验
        Object value = cellValueService.getValue(object, cell, excelParams, titleName, errorMsg);
        //对于类型转换有错误 不进行设置  使用类型的默认值
        if (StringUtils.isBlank(errorMsg)) {
            setValues(excelParams.get(titleName), object, value);
        }
    }

    /**
     * 功能描述:
     * 〈获取需要导入的字段〉
     *
     * @param fields
     * @param pojoClass
     * @return : void
     */
    public Map<String, ExcelImportEntity> getAllExcelField(Field[] fields, Class<?> pojoClass) {
        Map<String, ExcelImportEntity> importEntityMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Excel annotation = field.getAnnotation(Excel.class);
            if (annotation == null || !annotation.isImportField()) {
                continue;
            }
            //基本类型和枚举  pojo对象不支持
            if (PublicUtils.isJavaClass(field) || field.getType().isEnum()) {
                ExcelImportEntity importEntity = getImportEntity(field, pojoClass);
                importEntityMap.put(importEntity.getName(), importEntity);
            } else {
                LOGGER.debug("{} Class Type Import Is Not Supported", field.getName());
            }
        }
        return importEntityMap;
    }

    /**
     * 功能描述:
     * 〈〉
     *
     * @param field
     * @param pojoClass
     * @return : com.zl.excel.ExcelImportEntity
     */
    public ExcelImportEntity getImportEntity(Field field, Class<?> pojoClass) {
        Excel annotation = field.getAnnotation(Excel.class);
        ExcelImportEntity excelImportEntity = new ExcelImportEntity();
        excelImportEntity.setName(annotation.name());
        excelImportEntity.setImportDateFormat(annotation.importDateFormat());
        excelImportEntity.setImportEnumMethod(annotation.enumImportMethod());
//        excelImportEntity.setImportField(annotation.isImportField());
        excelImportEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getSetMethod(field.getName()));
        //importEntityMap.put(excelImportEntity.getName(), excelImportEntity);
        return excelImportEntity;
    }

    /**
     * 功能描述:
     * 〈获取表格字段列名对应的信息〉
     *
     * @param rows
     * @param importParams
     * @param excelParams  参数为使用(保留)
     * @return : java.util.Map<java.lang.Integer,java.lang.String>
     */
    public Map<Integer, String> getTitleMap(Iterator<Row> rows, ImportParams importParams, Map<String, ExcelImportEntity> excelParams) {
        Map<Integer, String> titleMap = new LinkedHashMap<>();
        for (int i = 0; i < importParams.getHeadRow(); i++) {
            Row row = rows.next();
            if (row == null) {
                continue;
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                String cellValue = getKeyValue(cell);
                if (StringUtils.isNotEmpty(cellValue)) {
                    titleMap.put(columnIndex, cellValue);
                }
            }
        }
        return titleMap;
    }

    public String getKeyValue(Cell cell) {
        return CellUtil.getCellValue(cell);
    }

    /**
     * 功能描述:
     * 〈表头校验  checkOrder为false时importFields设置无效 校验注解属性字段〉
     *
     * @param importParams
     * @param titleMap
     * @param excelParams
     * @param importResult
     * @return : boolean
     */
    public boolean verifyExcelTemplate(ImportParams importParams, Map<Integer, String> titleMap, Map<String, ExcelImportEntity> excelParams, ImportResult importResult) {
        String[] importFields = importParams.getImportFields();
        boolean verifyResult = true;
        if (importParams.getImportFields() != null && importParams.isCheckOrder()) {
            //序列的校验
            if (importFields.length != titleMap.size()) {
                LOGGER.error("Excel导入表头不一致");
                verifyResult = false;
            }
            int i = 0;
            for (String s : titleMap.values()) {
                if (!StringUtils.equals(s, importFields[i++])) {
                    LOGGER.error("Excel导入表头序列不一致");
                    verifyResult = false;

                }
            }
        }
        //没指定
        if (titleMap.size() != excelParams.size()) {
            LOGGER.error("Excel导入表头数量不一致");
            verifyResult = false;
        }
        for (String titleName : titleMap.values()) {
            if (!excelParams.containsKey(titleName)) {
                LOGGER.error("Excel导入表头内容不一致");
                verifyResult = false;

            }
        }
        if (!verifyResult) {
            List<String> errorMsgList = new ArrayList<>();
            errorMsgList.add("Excel导入模板错误");
            importResult.setVerifyMsg(errorMsgList);
        }
        return verifyResult;
    }


    public void setValues(ExcelImportEntity entity, Object object, Object value) throws Exception {
        // 去掉非包装类的异常情况
        if (value == null) {
            return;
        }
        entity.getMethod().invoke(object, value);
    }
}
