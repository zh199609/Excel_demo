package com.zl.entity;

import com.zl.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author albertzh
 */
public class User implements Serializable {

    private static final long serialVersionUID = 5131566962871242335L;

    @Excel(name = "编号")
    private Integer id;
    @Excel(name = "姓名")
    private String name;

    @Excel(name = "状态")//枚举重写toString
    private Status status;
    @Excel(name = "出生年月",exportDateFormat = "yyyy-MM-dd")
    private Date birthday;

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
}
