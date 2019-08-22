package com.zl.excel.style;

import com.zl.excel.ExcelExportEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * 导出style 接口
 */
public interface IExcelExportStyler {
    /**
     * 列表头样式
     */
    public CellStyle getHeaderStyle(short headerColor);

    /**
     * 标题样式
     */
    public CellStyle getTitleStyle(short color);

    /**
     * 获取样式方法
     */
    public CellStyle getStyles(boolean parity, ExcelExportEntity entity);

    /**
     * 获取样式方法
     *
     * @param dataRow 数据行
     * @param obj     对象
     * @param data    数据
     */
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data);
}
