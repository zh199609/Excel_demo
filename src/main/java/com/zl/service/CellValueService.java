package com.zl.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zl.entity.User;
import com.zl.excel.ExcelImportEntity;
import com.zl.util.PoiReflectorUtil;
import jdk.internal.org.objectweb.asm.signature.SignatureWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
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


    private Object getCellValue(String classFullName, Cell cell, ExcelImportEntity entity) {
        if (cell == null) {
            return "";
        }
        Object result = null;
        if ("class java.util.Date".equals(classFullName)) {
            if (CellType.NUMERIC == cell.getCellType() && DateUtil.isCellDateFormatted(cell)) {
                result = DateUtil.getJavaDate(cell.getNumericCellValue());
            } else {
                cell.setCellType(CellType.STRING);
                result = getDateData(entity, cell.getStringCellValue());
            }

        } else if (CellType.NUMERIC == cell.getCellType() && DateUtil.isCellDateFormatted(cell)) {
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
                           Map<String, ExcelImportEntity> entityMap, String titleName, StringBuilder errorMsg) {
        ExcelImportEntity entity = entityMap.get(titleName);
        String classFullName = "class java.lang.Object";
        Class clazz = null;
        Method method = entity.getMethod();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        //属性字段的返回类型
        classFullName = genericParameterTypes[0].toString();
        clazz = (Class) genericParameterTypes[0];
        Object result = getCellValue(classFullName, cell, entity);
        return getValueByType(classFullName, result, clazz, entity, errorMsg);
    }

    /**
     * 功能描述:
     * 〈根据类型获取cell的值 包含校验@Excel的简单校验〉
     *
     * @param classFullName
     * @param result
     * @param clazz
     * @param entity
     * @param errorMsg
     * @return : void
     */
    private Object getValueByType(String classFullName, Object result, Class clazz, ExcelImportEntity entity, StringBuilder errorMsg) {
        try {
            if (result == null || StringUtils.isBlank(result.toString())) {
                return null;
            }
            if ("class java.util.Date".equals(classFullName)) {
                return result;
            }
            if ("class java.util.Boolean".equals(classFullName) || "boolean".equals(classFullName)) {
                return Boolean.valueOf(String.valueOf(result));
            }
            if ("class java.util.Double".equals(classFullName) || "double".equals(classFullName)) {
                return Double.valueOf(String.valueOf(result));
            }
            if ("class java.lang.Float".equals(classFullName) || "float".equals(classFullName)) {
                return Float.valueOf(String.valueOf(result));
            }
            if ("class java.lang.Long".equals(classFullName) || "long".equals(classFullName)) {
                return Long.valueOf(String.valueOf(result));
            }
            if ("class java.lang.Integer".equals(classFullName) || "int".equals(classFullName)) {
                return Integer.valueOf(String.valueOf(result));
            }
            if ("class java.math.BigDecimal".equals(classFullName)) {
                return new BigDecimal(String.valueOf(result));
            }
            if ("class java.lang.String".equals(classFullName)) {
                //如果Excel中读取的不是String
                return String.valueOf(result);
            }
            if (clazz != null && clazz.isEnum()) {
                //TODO 参照其他框架的枚举反射
                String importEnumMethod = entity.getImportEnumMethod();
                if (StringUtils.isNotBlank(importEnumMethod)) {
                    return PoiReflectorUtil.forClass(clazz).execEnumStaticMethod(importEnumMethod, result);
                } else {
                    Enum.valueOf(clazz, result.toString());
                }
            }
            //TODO:其他类型 添加错误MSG
        } catch (Exception e) {
            LOGGER.error(clazz.getName() + "---getValueByType错误");
        }
        return result;
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


    /**
     * 功能描述:
     * 〈获取日期类型〉
     *
     * @param entity
     * @param value
     * @return : java.util.Date
     */
    private Date getDateData(ExcelImportEntity entity, String value) {
        if (StringUtils.isNotEmpty(entity.getImportDateFormat()) && StringUtils.isNotEmpty(value)) {
            //TODO 优化
            SimpleDateFormat format = new SimpleDateFormat(entity.getImportDateFormat());
            try {
                return format.parse(value);
            } catch (ParseException e) {
                LOGGER.error("时间格式化失败,格式化:{},值:{}", entity.getImportDateFormat(), value);
                throw new RuntimeException("获取cell值异常");
            }
        }
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Class clazz = User.class;
        Method getPwd = clazz.getMethod("setPwd", String.class);
        Type[] genericParameterTypes = getPwd.getGenericParameterTypes();
        String s = genericParameterTypes[0].toString();
        System.out.println(s);
    }

}
