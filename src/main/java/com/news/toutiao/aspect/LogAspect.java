package com.news.toutiao.aspect;


import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by huali on 2018/1/4.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.news.toutiao.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint)
    {
        //before and the after the logger all is be used in IndexController function.
        //通配符：*Controller
        StringBuilder sb=new StringBuilder();
        for(Object arg:joinPoint.getArgs())
        {
            sb.append("arg:"+arg.toString()+"|");
        }

        //logger.info("before : time:",new Date());
        logger.info("before method;"+sb.toString());

    }

    @After("execution(* com.news.toutiao.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint)
    {
        logger.info("after method;");
    }
}