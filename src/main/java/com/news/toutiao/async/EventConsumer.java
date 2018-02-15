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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huali on 2018/2/11.
 */
//专门是取出队列中的数据，数据取出以后，反序列化到当时的现场
    //有了model以后，在初始化时候，哪些event分别需要去处理的
    //然后再去找对应的handler，然后再去处理掉。
    //异步处理框架

@Service
//处理事情的都把他用作Service
public class EventConsumer  implements InitializingBean, ApplicationContextAware{
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);

    //spring依赖注入注解。到处所有接口。

    private Map<EventType,List<EventHandler>> config= new HashMap<EventType,List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans= applicationContext.getBeansOfType(EventHandler.class);
        //找出beans需要处理
        if(beans!=null)
        {
            for(Map.Entry<String,EventHandler> entry:beans.entrySet())
            {//遍历
                //取出EventType
                List<EventType> eventTypes=entry.getValue().getSupportEventType();
                //找出每个事件的handler，登记进来，
                for(EventType type:eventTypes)
                {
                    if(!config.containsKey(type))
                    {//复制新的进入，确保list不为空
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    //每个Event有几个handler要处理。
                    config.get(type).add(entry.getValue());

                }
            }
        }
        //开启线程完成队列
        Thread thread =new Thread(new Runnable(){
         @Override
         public void run()
         {
             while(true)
             {
                 String key= RedisKeyUtil.getEventQueueKey();
                 //取事件
                 List<String> events= jedisAdapter.brpop(0,key);
                 for(String message: events)
                 {
                     if(message.equals(key))
                     {
                         continue;
                     }
                     EventModel eventModel= JSON.parseObject(message,EventModel.class);
                     if(!config.containsKey(eventModel.getType()))
                     {
                         logger.error("不能识别事件");
                         continue;
                     }
                     for(EventHandler handler :config.get(eventModel.getType()))
                     {
                         handler.doHandler(eventModel);
                     }
                 }
             }
         }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;//applicaionContext.
    }
}