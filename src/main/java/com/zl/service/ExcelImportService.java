package com.zl.service;

import com.zl.annotation.Excel;
import com.zl.entity.User;
import com.zl.excel.ExcelImportEntity;
import com.zl.excel.ExcelImportUtil;
import com.zl.excel.ImportParams;
import com.zl.excel.ImportResult;
import com.zl.excel.verify.ExcelVerifyHandlerResult;
import com.zl.util.CellUtil;
import com.zl.util.PoiReflectorUtil;
import com.zl.util.PublicUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            while ((len = inputStream.read(bytes)) != 0) {
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
            //TODO
            importExcel(list, sheet, importParams, clazz, importResult);

            long endTime = System.currentTimeMillis();
        } catch (Exception e) {

        }

        return null;
    }


    public <T> List<T> importExcel(Collection<T> result, Sheet sheet, ImportParams importParams, Class<?> pojoClass, ImportResult importResult) {
        List collection = new ArrayList();
        Field[] classFields = PublicUtils.getClassFields(pojoClass);
        Map<String, ExcelImportEntity> excelParams = new HashMap<>();
        //获取导入字段
        getImportField(classFields, excelParams, pojoClass);
        Iterator<Row> rowIterator = sheet.rowIterator();
        for (int i = 0; i < importParams.getTitleRow(); i++) {
            rowIterator.next();
        }
        Map<Integer, String> titleMap = getTitleMap(rowIterator, importParams, excelParams);
        //模板校验错误直接返回
        boolean titleVerify = verifyExcelTemplate(importParams, titleMap, excelParams, importResult);
        if (titleVerify) {
            return null;
        }
        //从数据行开始读取  跳过无效的行数
        for (int i = 0; i < importParams.getDataRow(); i++) {
            rowIterator.next();
        }
        Map<String, Object> valMap;
        //数据开始读取
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            StringBuilder errorMsg = new StringBuilder();
            //数据对象创建
            Object object = PublicUtils.createObject(pojoClass);
            try {
                Set<Integer> titleIndex = titleMap.keySet();
                for (Integer index : titleIndex) {
                    Cell cell = row.getCell(index);
                    String titleName = titleMap.get(index);
                    Object value = cellValueService.getValue(object, cell, excelParams, titleName,errorMsg);

                }
            } catch (Exception e) {

            }

        }


        return null;
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
//        excelImportEntity.setImportField(annotation.isImportField());
        excelImportEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
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

}
