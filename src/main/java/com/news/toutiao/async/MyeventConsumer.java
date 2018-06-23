package com.news.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.news.toutiao.util.JedisAdapter;
import com.news.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by huali on 2018/6/23.
 */
public class MyeventConsumer implements InitializingBean, ApplicationContextAware{
    private static Logger logger = LoggerFactory.getLogger(MyeventConsumer.class);
    private   ApplicationContext applicationContext;
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    @Autowired
    JedisAdapter jedisAdapter;


    @Override
    public void afterPropertiesSet() throws Exception
    {
        Map<String ,EventHandler> beans= applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null)
        {
            for(Map.Entry<String, EventHandler> entry : beans.entrySet())
            {
                List<EventType> eventTypes = entry.getValue().getSupportEventType();
                for(EventType type:eventTypes)
                {
                    if(!config.containsKey(type))
                        config.put(type, new ArrayList<EventHandler>());
                    config.get(type).add(entry.getValue());  //EventHandler
                }

            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);  //推进去
                    for(String message: events)
                    {
                        if(message.equals(key))
                            continue;
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if(!config.containsKey(eventModel.getType()))
                        {
                            logger.error("不能识别的事件");
                            continue;

                        }
                        for(EventHandler handler:config.get(eventModel.getType()))
                        {
                            handler.doHandler(eventModel);
                        }

                    }

                }
            }
        });
        thread.start();

    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}