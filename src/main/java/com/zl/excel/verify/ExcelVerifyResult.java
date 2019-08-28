package com.zl.excel.verify;

import java.util.List;

/**
 * 校验结果信息
 */
public class ExcelVerifyResult {

    /**
     * 导入的数据总行数
     */
    private int importNum;
    /**
     * 失败的总行数
     */
    private int failNum;
    /**
     * 成功的数据行数
     */
    private int  SuccessNum;
    /**
     * 校验结果
     */
    private List<String> errorMsg;


}
