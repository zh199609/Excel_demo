package com.zl.controller;

import com.zl.entity.User;
import com.zl.excel.verify.ExcelVerifyHandlerResult;
import com.zl.excel.verify.IExcelVerifyHandler;

public class VerifyHandler implements IExcelVerifyHandler<User> {
    private ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();


    @Override
    public ExcelVerifyHandlerResult verifyHandler(User obj) {
        return result;
    }
}
