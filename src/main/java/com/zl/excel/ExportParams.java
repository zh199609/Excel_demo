package com.zl.excel;

import com.zl.enums.ExcelType;

/**
 * @ClassName: ExportParams
 * @Description: TODO 目前仅支持但sheet，导出参数设置
 * @Author: zl
 * @Date: 2019/8/1 21:29
 * @Version: 1.0
 **/
public class ExportParams {

    /**
     * 表格名称
     */
    private String title;

    /**
     * title高度
     */
    private short titleHeight = 10;


    /**
     * sheetName
     */
    private String sheetName;


    /**
     * Excel 导出版本
     */
    private ExcelType type = ExcelType.XSSF;

    /**
     * 表头高度
     */
    private double headerHeight = 9D;

    /**
     * 是否创建表头
     */
    private boolean isCreateHeadRows = true;

    /**
     * 导出时在excel中每个列的高度 单位为字符，一个汉字=2个字符
     * 全局设置,优先使用
     */
    public short height = 0;

    public ExportParams(String title, String sheetName) {
        this.title = title;
        this.sheetName = sheetName;
    }

    public ExportParams(String title, String sheetName, ExcelType type) {
        this.title = title;
        this.sheetName = sheetName;
        this.type = type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(short titleHeight) {
        this.titleHeight = titleHeight;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public ExcelType getType() {
        return type;
    }

    public void setType(ExcelType type) {
        this.type = type;
    }

    public double getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(double headerHeight) {
        this.headerHeight = headerHeight;
    }

    public boolean isCreateHeadRows() {
        return isCreateHeadRows;
    }

    public void setCreateHeadRows(boolean createHeadRows) {
        isCreateHeadRows = createHeadRows;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }
}
