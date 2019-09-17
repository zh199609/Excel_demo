package com.zl.entity;

/**
 * @ClassName Status
 * @Description
 * @Date 2019/8/23 9:46
 * @Author albertzh
 **/
public enum Status {
    VALID("有效"), INVALID("无效");


    private String name;

    Status(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Status importEnum(String s) {
        Status[] values = Status.values();
        for (Status status : values) {
            if (status.name.equals(s)) {
                return status;
            }
        }
        return null;
    }
}
