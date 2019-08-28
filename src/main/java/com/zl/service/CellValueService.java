package com.zl.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zl.excel.ExcelImportEntity;
import jdk.internal.org.objectweb.asm.signature.SignatureWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CellValueService
 * @Description: Cell取值服务
 * @Author: zl
 * @Date: 2019/8/28 20:56
 * @Version: 1.0
 **/
public class CellValueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CellValueService.class);

    private List<String> handlerList = null;


    private Object getCellValue(Cell cell, ExcelImportEntity entity) {
        if (cell == null) {
            return "";
        }
        Object result = null;
        if (CellType.NUMERIC == cell.getCellType() && DateUtil.isCellDateFormatted(cell)) {
            result = DateUtil.getJavaDate(cell.getNumericCellValue());
        } else {
            switch (cell.getCellType()) {
                case STRING:
                    result = cell.getRichStringCellValue() == null ? ""
                            : cell.getRichStringCellValue().getString();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        result = formateDate(entity, cell.getDateCellValue());
                    } else {
                        result = readNumericCell(cell);
                    }
                    break;
                case BOOLEAN:
                    result = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    /**
     * @return java.lang.Object
     * @Author i-leizh
     * @Description 获取Cell中的值
     * @Date 2019/8/28 21:24
     * @Param [object, cell, entityMap, titleName]
     **/
    public Object getValue(Object object, Cell cell,
                           Map<String, ExcelImportEntity> entityMap, String titleName) {
        ExcelImportEntity entity = entityMap.get(titleName);
        return getCellValue(cell, entity);
    }

    private String formateDate(ExcelImportEntity entity, Date value) {
        if (StringUtils.isNotEmpty(entity.getImportDateFormat()) && value != null) {
            //TODO 优化这个format
            SimpleDateFormat format = new SimpleDateFormat(entity.getImportDateFormat());
            return format.format(value);
        }
        return null;
    }

    private Object readNumericCell(Cell cell) {
        Object result = null;
        double value = cell.getNumericCellValue();
        if (((int) value) == value) {
            result = (int) value;
        } else {
            result = value;
        }
        return result;
    }

}
