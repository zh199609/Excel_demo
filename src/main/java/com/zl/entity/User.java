package com.zl.entity;

import java.io.Serializable;

/**
 * @author albertzh
 */
public class User implements Serializable {

    private static final long serialVersionUID = 5131566962871242335L;
    private Integer id;

    private String name;

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
