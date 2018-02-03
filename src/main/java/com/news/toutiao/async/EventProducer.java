package com.news.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.news.toutiao.util.JedisAdapter;
import com.news.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huali on 2018/2/3.
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel)
    {
        try {
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

}