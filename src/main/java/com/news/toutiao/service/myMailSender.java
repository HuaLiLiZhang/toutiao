package com.news.toutiao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by huali on 2018/6/23.
 */
public class myMailSender implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(myMailSender.class);
    private JavaMailSenderImpl mailSender;



    @Override    //java系统自带的邮件发送
    public void afterPropertiesSet() throws Exception {//在初始化的时候，
        // 就会调用InitializingBean的这个函数，然后对邮件的一些基本数据进行设置。
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("12432354576@qq.com");
        mailSender.setPassword("122345456");
        mailSender.setHost("smtp.exmail.qq.com");  //这里是发送邮箱的服务器，要去发送的邮箱查看。
        //这里是qq邮箱的一些协议。smtp 邮件传输协议
        mailSender.setPort(465);   //发送邮箱的端口
        mailSender.setProtocol("smpts");    //发送的协议smtp, 加一个ssl
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);

    }
}