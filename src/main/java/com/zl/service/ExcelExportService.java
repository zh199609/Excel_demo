package com.zl.service;

import com.zl.annotation.Excel;
import com.zl.enums.ExcelExportEnum;
import com.zl.enums.ExcelType;
import com.zl.excel.ExcelExportEntity;
import com.zl.excel.ExcelExportException;
import com.zl.excel.ExportParams;
import com.zl.util.PoiMergeCellUtil;
import com.zl.util.PoiReflectorUtil;
import com.zl.util.PublicUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * @Author i-leizh
 * @Description  导出服务
 * @Date 2019/8/1 21:34
 * @Param
 * @return
 **/
public class ExcelExportService {

    private int currentIndex = 0;

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
            //获取所有参数后
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
            sheet = workbook.createSheet(entity.getSheetName());
        } catch (Exception e) {
            e.printStackTrace();
            sheet = workbook.createSheet();
        }
        insertDataToSheet(workbook, entity, excelParams, dataSet, sheet);

    }

    private void insertDataToSheet(Workbook workbook, ExportParams entity, List<ExcelExportEntity> excelParams, Collection<?> dataSet, Sheet sheet) {
        List<ExcelExportEntity> entityList = new ArrayList<>();
        entityList.addAll(excelParams);
        sortAllParams(excelParams);
        // indexRow=1
        int indexRow = entity.isCreateHeadRows() ? createHeaderAndTitle(entity, sheet, workbook, excelParams) : 0;
        int titleIndex = indexRow;
        //设置宽度
        setCellWith(excelParams, sheet);
        //设置隐藏列
        setColumnHidden(excelParams, sheet);
        //获取行高
        short rowHeight = entity.getHeight() != 0 ? entity.getHeight() : getRowHeight(excelParams);
        setCurrentIndex(1);
        Iterator<?> iterator = dataSet.iterator();
        List<Object> tempList = new ArrayList<>();
        while (iterator.hasNext()) {
            Object t = iterator.next();
            indexRow += createCells(indexRow + 1, t, excelParams, sheet, workbook, rowHeight, 0)[0];
        }
    }

    /**
     * 创建 最主要的 Cells
     */
    public int[] createCells(int index, Object t,
                             List<ExcelExportEntity> excelParams, Sheet sheet, Workbook workbook,
                             short rowHeight, int cellNum) {
        try {
            ExcelExportEntity entity;
            Row row = sheet.getRow(index) == null ? sheet.createRow(index) : sheet.getRow(index);
            if (rowHeight != -1) {
                row.setHeight(rowHeight);
            }
            int maxHeight = 1;
            //合并需要合并的单元格
            int margeCellNum = cellNum;
//            createIndexCell
            for (int k = 0, paramSize = excelParams.size(); k < paramSize; k++) {
                entity = excelParams.get(k);
                Object cellValue = getCellValue(entity, t);
                //默认使用文本进行
                createStringCell(row, cellNum++, cellValue.toString(), entity);
            }
            return new int[]{maxHeight, cellNum};
        } catch (Exception e) {
            LOGGER.error("excel cell export error ,data is :{}", ReflectionToStringBuilder.toString(t));
            LOGGER.error(e.getMessage(), e);
            throw new ExcelExportException(ExcelExportEnum.EXPORT_ERROR, e);
        }

    }

    private int createIndexCell(Row row, int index, ExcelExportEntity excelExportEntity) {
        if (excelExportEntity.getName() != null && "序号".equals(excelExportEntity.getName())) {
            createStringCell(row, 0, currentIndex + "", null);
            currentIndex = currentIndex + 1;
            return 1;
        }
        return 0;
    }


    //获取填 如这个cell的值
    public Object getCellValue(ExcelExportEntity entity, Object obj) throws Exception {
        Object value;
        value = entity.getMethod().invoke(obj, new Object[]{});
        if (StringUtils.isNotEmpty(entity.getExportDateFormat())) {
            value = dateFormatValue(value, entity);
        }
        if (StringUtils.isNotEmpty(entity.getNumFormat())) {
            value = numFormatValue(value, entity);
        }

        if (StringUtils.isNotEmpty(entity.getSuffix()) && value != null) {
            value = value + entity.getSuffix();
        }
        if (value != null && StringUtils.isNotEmpty(entity.getEnumExportField())) {
            value = PoiReflectorUtil.fromCache(value.getClass()).getValue(value, entity.getEnumExportField());
        }
        return value == null ? "" : value.toString();
    }


    //对字段根据用户设置排序  默认OrderNum为0  默认顺序为字段顺序
    private void sortAllParams(List<ExcelExportEntity> excelParams) {
        Collections.sort(excelParams);
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
                //throw new RuntimeException("getAllExcelField方法异常了！");
                System.out.println(filed.getName() + "没有获取到@Excel注解");
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

    /**
     * 创建表头
     *
     * @param entity
     * @param sheet
     * @param workbook
     * @param excelParams
     * @return
     */
    protected int createHeaderAndTitle(ExportParams entity, Sheet sheet, Workbook workbook,
                                       List<ExcelExportEntity> excelParams) {
        int rows = 0, fieldLength = getFieldLength(excelParams);
        if (entity.getTitle() != null) {
            rows += createTitle2Row(entity, sheet, workbook, fieldLength);
        }
        createHeaderRow(entity, sheet, workbook, rows, excelParams, 0);

        if (entity.isFixedTitle()) {
            sheet.createFreezePane(0, rows, 0, rows);
        }
        return rows;
    }

    //创建 标题  合并单元格
    private int createTitle2Row(ExportParams entity, Sheet sheet, Workbook workbook, int fieldLength) {
        int indexRow = 0;
        Row row = sheet.createRow(indexRow);
        row.setHeight(entity.getTitleHeight());
        createStringCell(row, 0, entity.getTitle(), null);
        // cellIndex=0 为表头内容 cellIndex>0 为空字符串 用于合并单元格
        for (int i = 1; i < fieldLength; i++) {
            createStringCell(row, i, "", null);
        }
        //merge 标题行
        PoiMergeCellUtil.addMergedRegion(sheet, 0, 0, 0, fieldLength);
        //返回 1 新增一行标题
        return 1;
    }

    //设置标题  文本类型
    public void createStringCell(Row row, int i, String title, ExcelExportEntity entity) {
        Cell cell = row.createCell(i);
        RichTextString rtext;
        if (type.equals(ExcelType.HSSF)) {
            rtext = new HSSFRichTextString(title);
        } else {
            rtext = new XSSFRichTextString(title);
        }
        cell.setCellValue(rtext);
    }

    //创建表头
    private int createHeaderRow(ExportParams title, Sheet sheet, Workbook workbook, int index,
                                List<ExcelExportEntity> excelParams, int cellIndex) {

        Row row = sheet.getRow(index) == null ? sheet.createRow(index) : sheet.getRow(index);
        row.setHeight(title.getHeaderHeight());
        Row listRow = null;
        for (int i = 0, exportFieldSize = excelParams.size(); i < exportFieldSize; i++) {
            ExcelExportEntity excelExportEntity = excelParams.get(i);
            if (StringUtils.isNotBlank(excelExportEntity.getName())) {
                createStringCell(row, cellIndex, excelExportEntity.getName(), null);
            }
            cellIndex++;
        }

        return cellIndex;
    }

    public void setCellWith(List<ExcelExportEntity> excelParams, Sheet sheet) {
        int index = 0;
        for (int i = 0; i < excelParams.size(); i++) {
            sheet.setColumnWidth(index, (int) (256 * excelParams.get(i).getWidth()));
            index++;
        }
    }


    public void setColumnHidden(List<ExcelExportEntity> excelParams, Sheet sheet) {
        int index = 0;
        for (int i = 0; i < excelParams.size(); i++) {
            sheet.setColumnHidden(index, excelParams.get(i).isColumnHidden());
            index++;
        }
    }


    /**
     * 根据注解获取行高
     */
    public short getRowHeight(List<ExcelExportEntity> excelParams) {
        double maxHeight = 0;
        for (int i = 0; i < excelParams.size(); i++) {
            maxHeight = maxHeight > excelParams.get(i).getHeight() ? maxHeight
                    : excelParams.get(i).getHeight();
        }
        return (short) (maxHeight * 50);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }


    //时间格式化
    private Object dateFormatValue(Object value, ExcelExportEntity entity) throws Exception {
        Date temp = null;
        if (value instanceof String && StringUtils.isNoneEmpty(value.toString())) {
            SimpleDateFormat format = new SimpleDateFormat(entity.getExportDateFormat());
            temp = format.parse(value.toString());
        } else if (value instanceof Date) {
            temp = (Date) value;
        } else if (value instanceof java.sql.Date) {
            temp = new Date(((java.sql.Date) value).getTime());
        } else if (value instanceof java.sql.Time) {
            temp = new Date(((java.sql.Time) value).getTime());
        } else if (value instanceof java.sql.Timestamp) {
            temp = new Date(((java.sql.Timestamp) value).getTime());
        }
        if (temp != null) {
            SimpleDateFormat format = new SimpleDateFormat(entity.getExportDateFormat());
            value = format.format(temp);
        }
        return value;
    }

    //数字格式化
    private Object numFormatValue(Object value, ExcelExportEntity entity) {
        if (value == null) {
            return null;
        }
        if (!NumberUtils.isNumber(value.toString())) {
            LOGGER.error("data want num format ,but is not num, value is:" + value);
            return null;
        }
        Double d = Double.parseDouble(value.toString());
        DecimalFormat df = new DecimalFormat(entity.getNumFormat());
        return df.format(d);
    }
}
