package com.news.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huali on 2018/2/11.
 */
//代表一个个的事件,要触发的事件，，，
    //发生线程的一些数据，都打包在eventmodel中
public class EventModel {
    private EventType type; //事件类型
    private int actorId;   //事件触发者
    private int entityType;  //事件的触发对象
    private int entityId;  //事件的触发对象的Id
    private int entityOwnerId;   //触发对象的拥有者

    private Map<String ,String> exts=new HashMap<String ,String>();
    //现场数据存入，对象表示所有事件，事件类型，谁触发，触发对象是什么，触发拥有者。。。

    public EventModel(EventType type)
    {
        this.type= type;
    }

    public EventModel (){}


    public String getExt(String key)
    {
        return exts.get(key);
    }

    public EventModel setExt(String key,String value)
    {
        exts.put(key,value);
        return this;
    }



    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;

    }
}