package com.zl.excel;

import com.zl.annotation.Excel;

import java.lang.reflect.Method;

/**
 * 导入 映射
 */
public class ExcelImportEntity {
    /**
     * 表头Name
     */
    private String name;

    private Method method;

    /**
     * 导入字段校验
     */
    private boolean importField;
    /**
     * 时间格式
     */
    private String importDateFormat;
    /**
     * 导入枚举处理方法
     */
    private String importEnumMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isImportField() {
        return importField;
    }

    public void setImportField(boolean importField) {
        this.importField = importField;
    }

    public String getImportDateFormat() {
        return importDateFormat;
    }

    public void setImportDateFormat(String importDateFormat) {
        this.importDateFormat = importDateFormat;
    }

    public String getImportEnumMethod() {
        return importEnumMethod;
    }

    public void setImportEnumMethod(String importEnumMethod) {
        this.importEnumMethod = importEnumMethod;
    }
}
