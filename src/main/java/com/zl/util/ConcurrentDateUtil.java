package com.zl.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用ThreadLocal进行dateFormatter
 */
public class ConcurrentDateUtil {

    private static final String YMD = "yyyy-MM-dd";

    private static ThreadLocal<DateFormat> formatThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> formatStrThreadLocal = new ThreadLocal<>();


    private static DateFormat getDateFormat(String formatStr) {
        DateFormat dateFormat = formatThreadLocal.get();
        String ss = formatStrThreadLocal.get();
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(YMD);
            formatThreadLocal.set(dateFormat);
        }
        if (ss == null) {
            formatStrThreadLocal.set(formatStr);
        } else {
            if (!formatStrThreadLocal.get().equals(formatStr)){
                throw new RuntimeException("日期目标格式不一致");
            }
        }
        return dateFormat;
    }


    public String formatYMD(Date date, String formatStr) throws ParseException {
        return getDateFormat(formatStr).format(date);
    }

    public static Date parse(String strDate, String formatStr) throws ParseException {
        return getDateFormat(formatStr).parse(strDate);
    }
}
