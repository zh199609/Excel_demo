package com.zl.excel;

import com.zl.enums.ExcelExportEnum;

/**
 * @ClassName: ExcelExportException
 * @Description: TODO
 * @Author: zl
 * @Date: 2019/8/1 21:52
 * @Version: 1.0
 **/
public class ExcelExportException extends RuntimeException  {

    private ExcelExportEnum type;

    public ExcelExportException() {
        super();
    }

    public ExcelExportException(ExcelExportEnum type) {
        super(type.getMsg());
        this.type = type;
    }

    public ExcelExportException(ExcelExportEnum type, Throwable cause) {
        super(type.getMsg(), cause);
    }

    public ExcelExportException(String message) {
        super(message);
    }

    public ExcelExportException(String message, ExcelExportEnum type) {
        super(message);
        this.type = type;
    }

    public ExcelExportEnum getType() {
        return type;
    }

    public void setType(ExcelExportEnum type) {
        this.type = type;
    }
}
