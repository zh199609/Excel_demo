package com.zl.excel;

import com.zl.excel.verify.IExcelVerifyHandler;

/**
 * 导入参数设置
 */
public class ImportParams {
    /**
     * 标题行  默认0
     */
    private int titleRow = 0;
    /**
     * 表头行  默认0
     */
    private int headRow = 1;
    /**
     * 数据开始的行数
     */
    private int dataRow = 2;
    /**
     * sheet位置  默认0
     */
    private int sheetIndex = 0;
    /**
     * 校验
     */
    private Class[] verifyGroup = null;
    /**
     * 验证处理 返回errorMsg
     */
    private IExcelVerifyHandler excelVerifyHandler;
    /**
     * 导入模板字段  是都正确
     */
    private String[] importFields;
    /**
     * 导入字段是否校验序列  设置true必须设置importFields   @Excel中的Order属性用于导出   也可导入添加相应注解
     */
    private boolean CheckOrder = false;
    /**
     * 是否进行数据的校验   JSR303&自定义的校验器
     */
    private boolean isVerify = true;

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }

    public boolean isCheckOrder() {
        return CheckOrder;
    }

    public void setCheckOrder(boolean checkOrder) {
        CheckOrder = checkOrder;
    }

    public int getTitleRow() {
        return titleRow;
    }

    public void setTitleRow(int titleRow) {
        this.titleRow = titleRow;
    }

    public int getHeadRow() {
        return headRow;
    }

    public void setHeadRow(int headRow) {
        this.headRow = headRow;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Class[] getVerifyGroup() {
        return verifyGroup;
    }

    public void setVerifyGroup(Class[] verifyGroup) {
        this.verifyGroup = verifyGroup;
    }

    public IExcelVerifyHandler getExcelVerifyHandler() {
        return excelVerifyHandler;
    }

    public void setExcelVerifyHandler(IExcelVerifyHandler excelVerifyHandler) {
        this.excelVerifyHandler = excelVerifyHandler;
    }

    public String[] getImportFields() {
        return importFields;
    }

    public void setImportFields(String[] importFields) {
        this.importFields = importFields;
    }

    public int getDataRow() {
        return dataRow;
    }

    public void setDataRow(int dataRow) {
        this.dataRow = dataRow;
    }
}
