package com.news.toutiao.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huali on 2018/1/10.
 */
public class ViewObject {
    //试图展示的对象。方便传递任何数据到velocity。
    // 在homecontroller那里需要将数据进行打包，返回到home.html中
    private Map<String,Object> objs=new HashMap<>();
    public void set(String key,Object value)
    {
        objs.put(key,value);
    }
    public Object get(String key)
    {
        return objs.get(key);
    }
}