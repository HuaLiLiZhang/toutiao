package com.news.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.news.toutiao.util.JedisAdapter;
import com.news.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by huali on 2018/2/11.
 */
@Service
public class EventProducer {
    private static final Logger logger= LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    JedisAdapter jedisAdapter;


    public boolean fireEvent(EventModel model)
    {
        try {
            String json = JSONObject.toJSONString(model); //序列化
            //在util的package中RedisKeyUtil中加入一个key
            // :private static String BIZ_EVENT = "EVENT";

            String key = RedisKeyUtil.getEventQueueKey();//放入到队列中
            jedisAdapter.lpush(key, json);
            return true;

        }catch (Exception e)
        {
            logger.error("发生异常",e.getMessage());
            return false;
        }
    }
}