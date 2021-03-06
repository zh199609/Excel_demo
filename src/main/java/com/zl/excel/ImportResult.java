package com.zl.excel;

import com.zl.excel.verify.ExcelVerifyHandlerResult;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入结果返回
 */
public class ImportResult<T> {

    /**
     * 结果集 成功数据
     */
    private List<T> list;
    /**
     * 失败数据
     */
    private List<T> failList;
    /**
     * 是否存在校验失败 默认false
     */
    private boolean verfiyFail;
    /**
     * 数据源 成功的数据
     */
    private Workbook workbook;
    /**
     * 导入总的数据量
     */
    private int importNum;
    /**
     * 校验结果信息  错误按行
     */
    private List<String> verifyMsg;

    public ImportResult() {
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getFailList() {
        return failList;
    }

    public void setFailList(List<T> failList) {
        this.failList = failList;
    }

    public boolean isVerfiyFail() {
        return verfiyFail;
    }

    public void setVerfiyFail(boolean verfiyFail) {
        this.verfiyFail = verfiyFail;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public int getImportNum() {
        return importNum;
    }

    public void setImportNum(int importNum) {
        this.importNum = importNum;
    }

    public List<String> getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(List<String> verifyMsg) {
        this.verifyMsg = verifyMsg;
    }
}
