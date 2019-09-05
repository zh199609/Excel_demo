package com.zl.util;

import com.zl.entity.User;
import com.zl.service.ExcelImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PublicUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicUtils.class);

    /**
     * 获取class的 包括父类的 属性
     *
     * @param clazz
     * @return
     */
    public static Field[] getClassFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        Field[] fields;
        do {
            fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                list.add(fields[i]);
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);
        return list.toArray(fields);
    }


    /**
     * 功能描述:
     * 〈是不是Java基础类〉
     *
     * @param field
     * @return : boolean
     */
    public static boolean isJavaClass(Field field) {
        Class<?> fieldType = field.getType();
        boolean isBaseClass = false;
        if (fieldType.isArray()) {
            isBaseClass = false;
        } else if (fieldType.isPrimitive() || fieldType.getPackage() == null
                || "java.lang".equals(fieldType.getPackage().getName())
                || "java.math".equals(fieldType.getPackage().getName())
                || "java.sql".equals(fieldType.getPackage().getName())
                || "java.util".equals(fieldType.getPackage().getName())) {
            isBaseClass = true;
        }
        return isBaseClass;
    }

    /**
     * 功能描述:
     * 〈彻底创建一个对象〉
     *
     * @param clazz
     * @return : java.lang.Object
     */
    public static Object createObject(Class<?> clazz) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("创建对象异常");
        }
        return obj;
    }
}
