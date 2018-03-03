package com.news.toutiao.async;


/**
 * Created by huali on 2018/2/11.
 */
//表示发生了什么事件
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
    EventType(int value){this.value=value;}
    public int getValue(){return value;}
}
