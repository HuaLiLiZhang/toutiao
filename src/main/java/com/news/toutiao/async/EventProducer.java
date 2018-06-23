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
//发生了事件，就要把它发送出去，用来发送数据，
// 把数据序列化，然后放在redis的队列里面，就结束。
@Service
public class EventProducer {
    private static final Logger logger= LoggerFactory.getLogger(EventProducer.class);


    @Autowired
    JedisAdapter jedisAdapter;


    public boolean fireEvent(EventModel model)  //发一个事件
    {
        try {
            String json = JSONObject.toJSONString(model); //序列化
            //在util的package中RedisKeyUtil中加入一个key
            // :private static String BIZ_EVENT = "EVENT";

            String key = RedisKeyUtil.getEventQueueKey();//放入到队列中
            jedisAdapter.lpush(key, json);
            return true;   //事件产生者将事件推进去，事件key是EVENT， 类型是引入的事件类型，model

        }catch (Exception e)
        {
            logger.error("发生异常",e.getMessage());
            return false;
        }

    }
}