package com.zl.util;


import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//单元格 Merge 工具类
public class PoiMergeCellUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoiMergeCellUtil.class);



    public static void addMergedRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        try {
            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        } catch (Exception e) {
            LOGGER.error("发生了一次合并单元格错误,{},{},{},{}", new Integer[]{
                    firstRow, lastRow, firstCol, lastCol
            });
            // 忽略掉合并的错误,不打印异常
            LOGGER.debug(e.getMessage(), e);
        }
    }

}
