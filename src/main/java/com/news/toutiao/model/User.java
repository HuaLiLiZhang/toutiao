package com.news.toutiao.model;

/**
 * Created by huali on 2018/1/3.
 */
public class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public User(String name)
    {
        this.name=name;
    }



}