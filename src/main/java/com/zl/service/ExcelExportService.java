package com.zl.service;

import com.zl.annotation.Excel;
import com.zl.enums.ExcelExportEnum;
import com.zl.enums.ExcelType;
import com.zl.excel.ExcelExportEntity;
import com.zl.excel.ExcelExportException;
import com.zl.excel.ExportParams;
import com.zl.util.PoiReflectorUtil;
import com.zl.util.PublicUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * @Author i-leizh
 * @Description  导出服务
 * @Date 2019/8/1 21:34
 * @Param
 * @return
 **/
public class ExcelExportService {

    protected ExcelType type = ExcelType.XSSF;

    protected static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportService.class);


    public void createSheet(Workbook workbook, ExportParams entity, Class<?> pojoClass,
                            Collection<?> dataSet) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Excel export start ,class is {}", pojoClass);
            LOGGER.debug("Excel version is {}",
                    entity.getType().equals(ExcelType.HSSF) ? "03" : "07");
        }
        if (workbook == null || entity == null || pojoClass == null || dataSet == null) {
            throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
        }
        try {
            List<ExcelExportEntity> excelParams = new ArrayList<>();
            // 得到所有字段
            Field[] fileds = PublicUtils.getClassFields(pojoClass);
            getAllExcelField(fileds, excelParams, pojoClass,
                    null);
            //获取所有参数后,后面的逻辑判断就一致了
            createSheetForMap(workbook, entity, excelParams, dataSet);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e.getCause());
        }
    }

    private void createSheetForMap(Workbook workbook, ExportParams entity, List<ExcelExportEntity> excelParams, Collection<?> dataSet) {
        if (workbook == null || entity == null || excelParams == null || dataSet == null) {
            throw new ExcelExportException(ExcelExportEnum.PARAMETER_ERROR);
        }
        type = entity.getType();
        Sheet sheet = null;
        try {
            sheet=workbook.createSheet(entity.getSheetName());
        }catch (Exception e){
            e.printStackTrace();
            sheet = workbook.createSheet();
        }
        insertDataToSheet(workbook, entity, excelParams, dataSet, sheet);

    }

    private void insertDataToSheet(Workbook workbook, ExportParams entity, List<ExcelExportEntity> excelParams, Collection<?> dataSet, Sheet sheet) {
        List<ExcelExportEntity> entityList = new ArrayList<>();
        entityList.addAll(excelParams);
        sortAllParams(excelParams);
    }

    //对字段根据用户设置排序
    private void sortAllParams(List<ExcelExportEntity> excelParams) {
    }

    private void getAllExcelField(Field[] fileds, List<ExcelExportEntity> excelParams, Class<?> pojoClass, List<Method> getMethods) {
        ExcelExportEntity excelEntity;
        for (Field filed : fileds) {
            if (filed.getAnnotation(Excel.class) != null) {
                Excel excel = filed.getAnnotation(Excel.class);
                String name = excel.name();
                if (StringUtils.isNotBlank(name)) {
                    excelParams.add(createExcelExportEntity(filed, pojoClass, getMethods));
                }
            } else {
                throw new RuntimeException("getAllExcelField方法异常了！");
            }
        }
    }

    /**
     * 创建导出实体对象
     */
    private ExcelExportEntity createExcelExportEntity(Field field,
                                                      Class<?> pojoClass,
                                                      List<Method> getMethods) {
        Excel excel = field.getAnnotation(Excel.class);
        ExcelExportEntity excelEntity = new ExcelExportEntity();
        excelEntity.setType(excel.type());
        getExcelField(field, excelEntity, excel, pojoClass);
        if (getMethods != null) {
            List<Method> newMethods = new ArrayList<Method>();
            newMethods.addAll(getMethods);
            newMethods.add(excelEntity.getMethod());
            excelEntity.setMethods(newMethods);
        }
        return excelEntity;
    }

    //注解到导出对象的转换
    private void getExcelField(Field field, ExcelExportEntity excelEntity, Excel excel, Class<?> pojoClass) {
        excelEntity.setName(excel.name());
        excelEntity.setWidth(excel.width());
        excelEntity.setHeight(excel.height());
        excelEntity.setOrderNum(excel.orderNum());
        excelEntity.setSuffix(excel.suffix());
        excelEntity.setExportDateFormat(excel.exportDateFormat());
        excelEntity.setNumFormat(excel.numFormat());
        excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
        excelEntity.setColumnHidden(excel.isColumnHidden());
        excelEntity.setEnumExportField(excel.enumExportField());
    }


    /**
     * 获取导出报表的字段总长度
     */
    public int getFieldLength(List<ExcelExportEntity> excelParams) {
        int length = excelParams.size() - 1;// 从0开始计算单元格的
        return length;
    }


}
