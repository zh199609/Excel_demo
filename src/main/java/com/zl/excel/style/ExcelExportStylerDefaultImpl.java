package com.zl.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

//默认样式
public class ExcelExportStylerDefaultImpl extends AbstractExcelExportStyler implements IExcelExportStyler {

    public ExcelExportStylerDefaultImpl() {
    }

    public ExcelExportStylerDefaultImpl(Workbook workbook) {
        super.createStyles(workbook);
    }


    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //文本
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            cellStyle.setWrapText(true);
        }
        return cellStyle;
    }
}
