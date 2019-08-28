package com.zl.excel.verify;

public interface IExcelVerifyHandler<T> {

    /**
     * 导入校验方法
     * 
     * @param obj
     *            当前对象
     * @return
     */
    public ExcelVerifyHandlerResult verifyHandler(T obj);

}
