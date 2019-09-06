package com.zl.entity;

import com.zl.annotation.Excel;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author albertzh
 */
public class User implements Serializable {

    private static final long serialVersionUID = 5131566962871242335L;

    @Excel(name = "编号", isColumnHidden = false)
    @NotNull(message = "编号不能为空")
    private Integer id;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "状态", enumImportMethod = "importEnum")//枚举重写toString
    private Status status;
    @Excel(name = "出生年月", exportDateFormat = "yyyy-MM-dd", importDateFormat = "yyyy-MM-dd")
    private Date birthday;
    @Excel(name = "更新日期", exportDateFormat = "yyyy-MM-dd HH:mm:ss", orderNum = 2, importDateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @Excel(name = "价格", numFormat = "#0.00", orderNum = 1)
    private BigDecimal price;
    @Excel(name = "Double包装类型")
    private Double aDouble;
    @Excel(name = "Float包装类型")
    private Float aFloat;
    @Excel(name = "Long包装类型")
    private Long aLong;
    @Excel(name = "Boolean包装类型")
    private Boolean aBoolean;
    @Excel(name = "double基本类型")
    private double bDouble;
    @Excel(name = "float基本类型")
    private float bFloat;
    @Excel(name = "long基本类型")
    private long bLong;
    @Excel(name = "boolean基本类型")
    private boolean bBoolean;
    @Excel(name = "int基本类型")
    private int bInt;
    private String pwd;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getaDouble() {
        return aDouble;
    }

    public void setaDouble(Double aDouble) {
        this.aDouble = aDouble;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public Long getaLong() {
        return aLong;
    }

    public void setaLong(Long aLong) {
        this.aLong = aLong;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public double getbDouble() {
        return bDouble;
    }

    public void setbDouble(double bDouble) {
        this.bDouble = bDouble;
    }

    public float getbFloat() {
        return bFloat;
    }

    public void setbFloat(float bFloat) {
        this.bFloat = bFloat;
    }

    public long getbLong() {
        return bLong;
    }

    public void setbLong(long bLong) {
        this.bLong = bLong;
    }

    public boolean isbBoolean() {
        return bBoolean;
    }

    public void setbBoolean(boolean bBoolean) {
        this.bBoolean = bBoolean;
    }

    public int getbInt() {
        return bInt;
    }

    public void setbInt(int bInt) {
        this.bInt = bInt;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", birthday=" + birthday +
                ", updateTime=" + updateTime +
                ", price=" + price +
                ", aDouble=" + aDouble +
                ", aFloat=" + aFloat +
                ", aLong=" + aLong +
                ", aBoolean=" + aBoolean +
                ", bDouble=" + bDouble +
                ", bFloat=" + bFloat +
                ", bLong=" + bLong +
                ", bBoolean=" + bBoolean +
                ", bInt=" + bInt +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
