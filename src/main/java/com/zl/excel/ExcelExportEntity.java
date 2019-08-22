package com.zl.excel;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @ClassName: ExcelExportEntity
 * @Description: TODO excel 导出工具类,对cell类型做映射
 * @Author: zl
 * @Date: 2019/8/1 21:36
 * @Version: 1.0
 **/
public class ExcelExportEntity implements Comparable<ExcelExportEntity> {

    private String name;

    private String numFormat;
    /**
     * 是否隐藏列
     */
    private boolean isColumnHidden;

    /**
     * 枚举导出属性字段
     */
    private String enumExportField;

    /**
     * 排序顺序
     */
    private int orderNum = 0;

    //宽度
    private double width = 10;
    //高度
    private double height = 10;

    //类型
    private int type = 1;
    //后缀
    private String suffix;
    //导出日期格式
    private String exportDateFormat;

    private List<Method> methods;

    /**
     * set/get方法
     */
    private Method method;

    /**
     * 是否支持换行
     */
    private boolean isWrap = true;


    @Override
    public int compareTo(ExcelExportEntity o) {
        return this.getOrderNum() - o.getOrderNum();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumFormat() {
        return numFormat;
    }

    public void setNumFormat(String numFormat) {
        this.numFormat = numFormat;
    }

    public boolean isColumnHidden() {
        return isColumnHidden;
    }

    public void setColumnHidden(boolean columnHidden) {
        isColumnHidden = columnHidden;
    }

    public String getEnumExportField() {
        return enumExportField;
    }

    public void setEnumExportField(String enumExportField) {
        this.enumExportField = enumExportField;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getExportDateFormat() {
        return exportDateFormat;
    }

    public void setExportDateFormat(String exportDateFormat) {
        this.exportDateFormat = exportDateFormat;
    }

    public boolean isWrap() {
        return isWrap;
    }

    public void setWrap(boolean wrap) {
        isWrap = wrap;
    }
}
