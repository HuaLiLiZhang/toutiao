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
@Aspect  //定义一个切面。
@Component
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    //面向切面编程，对日志进行统一格式的打印，但是又不能在每一个Controller添加，比较麻烦。
    //所以这里使用AOP：面向切面的编程，定义一个log日志的类，统一处理，调用的函数的并打印日志

    //这里在所有执行controller包下的所有类 之前和 之后 都要调用这两种方法。
    @Before("execution(* com.news.toutiao.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint)  //JoinPoint：表示切点。
    {
        //before and the after the logger all is be used in IndexController function.
        //通配符：*Controller ：表示Controller前面任意字符
        //格式为execution(* 包名(..))  /这里* ：*后面有一个空格， (..)表示包下所有方法。
        StringBuilder sb=new StringBuilder();
        for(Object arg:joinPoint.getArgs())  //这里是切面的参数，也就是输入到地址栏的参数。
        {
            sb.append("arg:"+arg.toString()+"|");
        }

        //比如访问：http://127.0.0.1:8080/admin?key=admind
        //参数  admind ,所以jointpoint的getArgs()是before method;arg:admind|
        //日志结果：before method;arg:admind|
        // after method;
        //由于访问错误，throw new IllegalArgumentException()调用了异常捕捉函数。
        //结果：before method;arg:java.lang.IllegalArgumentException: key error错误 !!! 请重新输入key_|
        //after method;

        //logger.info("before : time:",new Date());
        logger.info("before method;"+sb.toString());

    }

    @After("execution(* com.news.toutiao.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint)
    {
        logger.info("after method;");
    }
}