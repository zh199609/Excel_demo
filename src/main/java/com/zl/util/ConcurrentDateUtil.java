package com.zl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用ThreadLocal进行dateFormatter
 */
public class ConcurrentDateUtil {

    public static final String YMD = "yyyy-MM-dd";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm";

    private static ThreadLocal<Map<String, DateFormat>> formatThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> formatStrThreadLocal = new ThreadLocal<>();


    private static DateFormat getDateFormat(String formatStr) {
        Map<String, DateFormat> stringDateFormatMap = formatThreadLocal.get();
        String ss = formatStrThreadLocal.get();
        if (stringDateFormatMap == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
            Map<String, DateFormat> map = new HashMap<>();
            map.put(formatStr, simpleDateFormat);
            formatThreadLocal.set(map);
        } else if (!stringDateFormatMap.containsKey(formatStr)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
            stringDateFormatMap.put(formatStr, simpleDateFormat);
        }
        /*if (ss == null) {
            formatStrThreadLocal.set(formatStr);
        } else {
            if (!formatStrThreadLocal.get().equals(formatStr)) {
                throw new RuntimeException("日期目标格式不一致");
            }
        }*/
        return formatThreadLocal.get().get(formatStr);
    }


    public static String formatDate(Date date, String formatStr) {
        return getDateFormat(formatStr).format(date);
    }

    public static Date parseDateStr(String strDate, String formatStr) throws ParseException {
        return getDateFormat(formatStr).parse(strDate);
    }


    public static void main(String[] args) throws ParseException {
        String strDate ="2019-09-25 9:30";
        Date date = parseDateStr(strDate, YMDHMS);
        System.out.println(date.toString());
    }

}
