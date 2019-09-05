package com.zl.excel.style;

import com.zl.excel.ExcelExportEntity;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class AbstractExcelExportStyler implements IExcelExportStyler {

    //单行    此变量缓存的
    protected CellStyle stringNoneStyle;
    //是否包装文本
    protected CellStyle stringNoneWrapStyle;

    protected Workbook workbook;

    protected static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");

    protected void createStyles(Workbook workbook) {
        this.workbook = workbook;
        this.stringNoneStyle = stringNoneStyle(false);
        this.stringNoneWrapStyle = stringNoneStyle(true);
    }


    public CellStyle stringNoneStyle(boolean isWarp) {
        return null;
    }

    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
        if (noneStyler && (entity == null || entity.isWrap())) {
            return stringNoneWrapStyle;
        }
        return stringNoneStyle;
    }

    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(dataRow % 2 == 1, entity);
    }

}
