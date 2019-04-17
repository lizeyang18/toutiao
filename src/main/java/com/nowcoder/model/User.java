package com.nowcoder.model;

/**
 * Created by nowcoder on 2016/6/26.
 */
public class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public User(String name) {
        this.name = name;
    }
}
