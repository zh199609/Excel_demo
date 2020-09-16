package com.zl.entity;

import com.zl.annotation.Excel;

public class Test {
    @Excel(name = "编号1")
    private String name;
    @Excel(name = "编号2")
    private String a;
    @Excel(name = "编号3")
    private String b;
    @Excel(name = "编号4")
    private String c;
    @Excel(name = "编号5")
    private String d;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}
