package com.news.toutiao.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huali on 2018/1/10.
 */
public class ViewObject {
    private Map<String,Object> objs=new HashMap<>();
    public void set(String key,Object value){objs.put(key,value);}
    public Object get(String key){return objs.get(key);}
}