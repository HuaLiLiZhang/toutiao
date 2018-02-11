package com.news.toutiao.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by huali on 2018/2/11.
 */
@Component
//处理事情的都把他用作Component
public class EventConsumer  implements InitializingBean, ApplicationContextAware{
    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);


    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        
    }
}