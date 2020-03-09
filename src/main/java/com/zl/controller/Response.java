package com.zl.controller;

/**
 * @ClassName: Response
 * @Description: TODO
 * @Author: zl
 * @Date: 2020/3/8 15:40
 * @Version: 1.0
 **/
public class Response {
    private String code;
    private String message;
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
