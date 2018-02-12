package com.news.toutiao.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huali on 2018/2/11.
 */
@Component
//处理事情的都把他用作Component
public class EventConsumer  implements InitializingBean, ApplicationContextAware{
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType,List<EventHandler>> config= new HashMap<EventType,List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans= applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null)
        {
            for(Map.Entry<String,EventHandler> entry:beans.entrySet())
            {
                List<EventType> eventTypes=entry.getValue().getSupportEventType();
                for(EventType type:eventTypes)
                {
                    if(!config.containsKey(type))
                    {
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}