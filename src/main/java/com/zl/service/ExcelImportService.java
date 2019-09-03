package com.zl.service;

import com.zl.annotation.Excel;
import com.zl.entity.User;
import com.zl.excel.ExcelImportEntity;
import com.zl.excel.ExcelImportUtil;
import com.zl.excel.ImportParams;
import com.zl.excel.ImportResult;
import com.zl.excel.verify.ExcelVerifyHandlerResult;
import com.zl.excel.verify.IExcelVerifyHandler;
import com.zl.util.CellUtil;
import com.zl.util.PoiReflectorUtil;
import com.zl.util.PublicUtils;
import com.zl.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 导入服务
 */
public class ExcelImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportService.class);

    private CellValueService cellValueService;

    public ExcelImportService() {
        this.cellValueService = new CellValueService();
    }

    public ImportResult importExcelBase(InputStream inputStream, Class<?> clazz, ImportParams importParams) {
        LOGGER.debug("Excel import start,ClassType is {}", clazz);
        List<T> list = new ArrayList<>();
        ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
        ImportResult importResult = new ImportResult();
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
            //参数list并没用使用
            list.addAll(importExcel(list, sheet, importParams, clazz, importResult));
            //TODO  如何进行数据的返回
            if (importResult.isVerfiyFail()) {
                return null;
            }
            importResult.setList(list);
            long endTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return importResult;
    }


    public <T> List<T> importExcel(Collection<T> result, Sheet sheet, ImportParams importParams, Class<?> pojoClass, ImportResult importResult) {
        List collection = new ArrayList();
        Field[] classFields = PublicUtils.getClassFields(pojoClass);
        Map<String, ExcelImportEntity> excelParams = new HashMap<>();
        //获取导入字段
        getImportField(classFields, excelParams, pojoClass);
        Iterator<Row> rowIterator = sheet.rowIterator();
        //跳过title和head的行
        for (int i = 0; i <= importParams.getTitleRow(); i++) {
            Row next = rowIterator.next();
        }
        Map<Integer, String> titleMap = getTitleMap(rowIterator, importParams, excelParams);
        //模板校验错误直接返回
        boolean titleVerify = verifyExcelTemplate(importParams, titleMap, excelParams, importResult);
        if (titleVerify) {
            return null;
        }
        //从数据行开始读取  跳过无效的行数
        for (int i = importParams.getHeadRow(); i < importParams.getDataRow() - 1; i++) {
            rowIterator.next();
        }
        Map<String, Object> valMap;
        //数据开始读取
        Row row;
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
                //数据校验
                if (verifyData(object, row, importParams, errorMsg)) {
                    collection.add(object);
                }
            } catch (Exception e) {
                //TODO  所有的异常进行处理
                e.printStackTrace();
            }
        }
        return collection;
    }

    public boolean verifyData(Object object, Row row, ImportParams importParams, StringBuilder errorMsg) {
        //true正确
        boolean verifyResult = true;
//        errorMsg.append("第" + row.getRowNum() + "行数据：");
        if (importParams.isVerify()) {
            //JSR303验证
            String validationMsg = ValidatorUtil.validation(object);
            if (StringUtils.isNotBlank(validationMsg)) {
                errorMsg.append(errorMsg);
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
        return verifyResult;
    }

    private void setFieldValue(Object object, Cell cell, Map<String, ExcelImportEntity> excelParams, String titleName, StringBuilder errorMsg) throws Exception {
        //获取属性值  包含类型的校验
        Object value = cellValueService.getValue(object, cell, excelParams, titleName, errorMsg);
        setValues(excelParams.get(titleName), object, value);
    }

    /**
     * 功能描述:
     * 〈获取需要导入的字段〉
     *
     * @param fields
     * @param importEntitys
     * @param pojoClass
     * @return : void
     */
    public void getImportField(Field[] fields, Map<String, ExcelImportEntity> importEntitys, Class<?> pojoClass) {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            //TODO 我的天。。。。。。
            getAllExcelField(fields, importEntitys, pojoClass);
        }
    }


    public void getAllExcelField(Field[] fields, Map<String, ExcelImportEntity> importEntityMap, Class<?> pojoClass) {
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Excel annotation = field.getAnnotation(Excel.class);
            if (annotation == null || !annotation.isImportField()) {
                continue;
            }
            //基本类型和枚举  pojo对象不支持
            //TODO  Collection去掉
            if (PublicUtils.isJavaClass(field) || field.getType().isEnum()) {
                ExcelImportEntity importEntity = getImportEntity(field, pojoClass);
                importEntityMap.put(importEntity.getName(), importEntity);
            } else {
                LOGGER.debug("{} Class Type Import Is Not Supported", field.getName());
            }
        }
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
     * TODO: checkOrder为false时也进行importFields的校验
     *
     * @param importParams
     * @param titleMap
     * @param excelParams
     * @param importResult
     * @return : boolean
     */
    public boolean verifyExcelTemplate(ImportParams importParams, Map<Integer, String> titleMap, Map<String, ExcelImportEntity> excelParams, ImportResult importResult) {
        String[] importFields = importParams.getImportFields();
        boolean verifyResult = false;
        importResult.setVerfiyFail(true);
        if (importParams.getImportFields() != null && importParams.isCheckOrder()) {
            //序列的校验
            if (importFields.length != titleMap.size()) {
                LOGGER.error("Excel导入表头不一致");
                importResult.getVerifyMsg().put(0, "Excel导入模板错误");
                verifyResult = false;
                return verifyResult;
            }
            int i = 0;
            for (String s : titleMap.values()) {
                if (!StringUtils.equals(s, importFields[i++])) {
                    LOGGER.error("Excel导入表头序列不一致");
                    verifyResult = false;
                    importResult.getVerifyMsg().put(0, "Excel导入模板错误");
                    return verifyResult;
                }
            }
        }
        //没指定
        if (titleMap.size() != excelParams.size()) {
            LOGGER.error("Excel导入表头数量不一致");
            verifyResult = false;
            importResult.getVerifyMsg().put(0, "Excel导入模板错误");
            return verifyResult;
        }
        for (String titleName : titleMap.values()) {
            if (!excelParams.containsKey(titleName)) {
                LOGGER.error("Excel导入表头内容不一致");
                verifyResult = false;
                importResult.getVerifyMsg().put(0, "Excel导入模板错误");
            }
        }
        importResult.setVerfiyFail(false);
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
