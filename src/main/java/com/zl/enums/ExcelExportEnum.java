package com.zl.enums;

/**
 * @ClassName: ExcelExportEnum
 * @Description:
 * @Author: zl
 * @Date: 2019/8/1 21:51
 * @Version: 1.0
 **/
public enum ExcelExportEnum {


    PARAMETER_ERROR("Excel 导出   参数错误"),
    EXPORT_ERROR("Excel导出错误"),
    TEMPLATE_ERROR("Excel 模板错误");

    private String msg;

    ExcelExportEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
