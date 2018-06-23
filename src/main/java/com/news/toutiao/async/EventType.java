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
    private EventType(int value){this.value=value;}  //枚举类型的构造函数默认为private
    public int getValue(){return value;}
}
