package com.zl.entity;

import com.zl.annotation.Excel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 导出类测试
 */
public class ExportEntity {
    @Excel(name = "byteWarp")
    private Byte byteWarp;
    @Excel(name = "byteBasic", width = 15D)
    private byte byteBasic;
    @Excel(name = "shortWarp", orderNum = 1)
    private Short shortWarp;
    @Excel(name = "shortBasic", height = 15D)
    private short shortBasic;
    @Excel(name = "intWarp", isExportField = false)
    private Integer intWarp;
    @Excel(name = "intBasic", isWrap = false)
    private int intBasic;
    @Excel(name = "longWrap", isColumnHidden = true)
    private Long longWrap;
    @Excel(name = "longBasic")
    private long longBasic;
    @Excel(name = "floatWrap")
    private Float floatWrap;
    @Excel(name = "floatBasic")
    private float floatBasic;
    @Excel(name = "doubleWrap")
    private Double doubleWrap;
    @Excel(name = "doubleBasic")
    private double doubleBasic;
    @Excel(name = "booleanWrap")
    private Boolean booleanWarp;
    @Excel(name = "booleanBasic")
    private boolean booleanBasic;
    @Excel(name = "charBasic")
    private char charBasic;
    @Excel(name = "charWrap")
    private Character charWrap;
    @Excel(name = "string", suffix = "字符串")
    private String string;
    @Excel(name = "date", exportDateFormat = "yyyy-MM-dd")
    private Date date;
    @Excel(name = "bigDecimal", numFormat = "0.00")
    private BigDecimal bigDecimal;
    @Excel(name = "status")
    //TODO 导出时枚举指定某个属性
    private Status status;

    public Byte getByteWarp() {
        return byteWarp;
    }

    public void setByteWarp(Byte byteWarp) {
        this.byteWarp = byteWarp;
    }

    public byte getByteBasic() {
        return byteBasic;
    }

    public void setByteBasic(byte byteBasic) {
        this.byteBasic = byteBasic;
    }

    public Short getShortWarp() {
        return shortWarp;
    }

    public void setShortWarp(Short shortWarp) {
        this.shortWarp = shortWarp;
    }

    public short getShortBasic() {
        return shortBasic;
    }

    public void setShortBasic(short shortBasic) {
        this.shortBasic = shortBasic;
    }

    public Integer getIntWarp() {
        return intWarp;
    }

    public void setIntWarp(Integer intWarp) {
        this.intWarp = intWarp;
    }

    public int getIntBasic() {
        return intBasic;
    }

    public void setIntBasic(int intBasic) {
        this.intBasic = intBasic;
    }

    public Long getLongWrap() {
        return longWrap;
    }

    public void setLongWrap(Long longWrap) {
        this.longWrap = longWrap;
    }

    public long getLongBasic() {
        return longBasic;
    }

    public void setLongBasic(long longBasic) {
        this.longBasic = longBasic;
    }

    public Float getFloatWrap() {
        return floatWrap;
    }

    public void setFloatWrap(Float floatWrap) {
        this.floatWrap = floatWrap;
    }

    public float getFloatBasic() {
        return floatBasic;
    }

    public void setFloatBasic(float floatBasic) {
        this.floatBasic = floatBasic;
    }

    public Double getDoubleWrap() {
        return doubleWrap;
    }

    public void setDoubleWrap(Double doubleWrap) {
        this.doubleWrap = doubleWrap;
    }

    public double getDoubleBasic() {
        return doubleBasic;
    }

    public void setDoubleBasic(double doubleBasic) {
        this.doubleBasic = doubleBasic;
    }

    public Boolean getBooleanWarp() {
        return booleanWarp;
    }

    public void setBooleanWarp(Boolean booleanWarp) {
        this.booleanWarp = booleanWarp;
    }

    public boolean isBooleanBasic() {
        return booleanBasic;
    }

    public void setBooleanBasic(boolean booleanBasic) {
        this.booleanBasic = booleanBasic;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public char getCharBasic() {
        return charBasic;
    }

    public void setCharBasic(char charBasic) {
        this.charBasic = charBasic;
    }

    public Character getCharWrap() {
        return charWrap;
    }

    public void setCharWrap(Character charWrap) {
        this.charWrap = charWrap;
    }

    public static void main(String[] args) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        System.out.println(decimalFormat.format(17668.5889184D));
    }
}
