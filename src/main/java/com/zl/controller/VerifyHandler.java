package com.zl.controller;

import com.zl.entity.Test;
import com.zl.excel.verify.ExcelVerifyHandlerResult;
import com.zl.excel.verify.IExcelVerifyHandler;

public class VerifyHandler implements IExcelVerifyHandler<Test> {
    private ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();


    @Override
    public ExcelVerifyHandlerResult verifyHandler(Test obj) {
        if (obj.getName().equals("445")) {

        }
        if (obj.getA().equals("adf")) {

        }
        return result;
    }
}
