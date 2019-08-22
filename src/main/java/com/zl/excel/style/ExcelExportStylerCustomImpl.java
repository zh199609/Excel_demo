package com.zl.excel.style;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelExportStylerCustomImpl extends AbstractExcelExportStyler implements IExcelExportStyler {

    //字体
    private static final String FONT_NAME = "微软雅黑";

    public ExcelExportStylerCustomImpl() {
    }

    public ExcelExportStylerCustomImpl(Workbook workbook) {
        super.createStyles(workbook);
    }


    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName(FONT_NAME);
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderBottom(BorderStyle.THIN);//下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName(FONT_NAME);
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderBottom(BorderStyle.THIN);//下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //文本
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 8);
        font.setFontName(FONT_NAME);
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderBottom(BorderStyle.THIN);//下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            cellStyle.setWrapText(true);
        }
        return cellStyle;
    }
}
