package com.zl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导入导出注解
 *
 * @author albertzh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    /**
     * 导出时，对应数据库的字段 主要是用户区分每个字段， 不能有annocation重名的 导出时的列名
     * 导出排序跟定义了annotation的字段的顺序有关 可以使用a_id,b_id来确实是否使用
     */
    public String name();

    //后缀格式
    public String suffix() default "";

    //数字格式化，参数是Pattern,使用的对象是DecimalFormat
    public String numFormat() default "";

    //导出时间格式化 以这个是否为空来判断是否需要格式化日期
    String exportDateFormat() default "";

    //导入时间格式化
    String importDateFormat() default "";

    //高度，导出时在excel中每个列的高度 单位为字符，一个汉字=2个字符
    double height() default 10.0D;

    //宽度
    double width() default 10.0D;

    //顺序
    int orderNum() default 0;

    //是否需要隐藏该列
    boolean isColumnHidden() default false;

    //枚举导出使用的字段
    String enumExportField() default "";

    String enumImportMethod() default "";

    //1:文本
    public int type() default 1;

    //支持换行\n  WrapStyle
    public boolean isWrap() default true;

    //导出字段  默认是
    boolean isExportField() default true;

    //导入字段  默认是
    boolean isImportField() default true;

}
