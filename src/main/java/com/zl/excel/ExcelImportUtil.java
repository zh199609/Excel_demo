package com.zl.excel;


import com.zl.entity.User;
import com.zl.service.ExcelImportService;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 导出工具类
 */
public class ExcelImportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportUtil.class);

    public static <T> ImportResult<T> importExcel(InputStream inputStream, Class<?> clazz, ImportParams importParams) {
        return new ExcelImportService().importExcelBase(inputStream, clazz, importParams);
    }

}
