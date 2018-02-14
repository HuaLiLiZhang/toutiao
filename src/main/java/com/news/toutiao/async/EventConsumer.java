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

    //spring依赖注入注解。到处所有接口。

    private Map<EventType,List<EventHandler>> config= new HashMap<EventType,List<EventHandler>>();
    private ApplicationContext applicationContext;

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
                    //
                    config.get(type).add(entry.getValue());

                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;//applicaionContext.
    }
}