package com.zl.excel;

import com.zl.enums.ExcelType;
import com.zl.service.ExcelExportService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;

/**
 * @author albertzh
 */
public class ExcelExportUtil {

    private static Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < 100000) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

    /**
     *
     * @param entity    表格标题属性
     * @param pojoClass excel的class对象
     * @param dataSet   excel的数据集
     * @return
     */
    public static Workbook exportExcel(ExportParams entity, Class<?> pojoClass,
                                       Collection<?> dataSet) {
        Workbook workbook = getWorkbook(entity.getType(), dataSet.size());
        new ExcelExportService().createSheet(workbook, entity, pojoClass, dataSet);
        return workbook;
    }



}
