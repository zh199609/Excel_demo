package com.zl.entity;

import com.zl.annotation.Excel;

import java.io.Serializable;

/**
 * @author albertzh
 */
public class User implements Serializable {

    private static final long serialVersionUID = 5131566962871242335L;

    @Excel(name = "编号")
    private Integer id;
    @Excel(name="姓名")
    private String name;

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
}
