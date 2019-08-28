package com.zl.excel.verify;

/**
 * 校验结果
 */
public class ExcelVerifyHandlerResult {
    /**
     * 是否正确
     */
    private boolean success;
    /**
     * 错误信息
     */
    private String msg;

    public ExcelVerifyHandlerResult() {

    }


    public ExcelVerifyHandlerResult(boolean success) {
        this.success = success;
    }

    public ExcelVerifyHandlerResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}