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

    @Excel(name = "编号", isColumnHidden = true)
    private Integer id;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "状态")//枚举重写toString
    private Status status;
    @Excel(name = "出生年月", exportDateFormat = "yyyy-MM-dd")
    private Date birthday;
    @Excel(name = "更新日期", exportDateFormat = "yyyy-MM-dd HH:mm:ss", orderNum = 2)
    private Date updateTime;
    @Excel(name = "价格", numFormat = "#0.00", orderNum = 1)
    @Max(value = 10, message = "价格超出范围")
    private BigDecimal price;

    private String pwd;

    public User() {
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
