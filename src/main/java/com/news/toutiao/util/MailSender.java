package com.news.toutiao.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;


/**
 * Created by huali on 2018/3/3.
 */
@Service
public class MailSender implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;


    @Autowired
    private VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to,String subject,
                                        String template,Map<String,Object> model)
    {
        try {//这里随便写自己的邮箱的昵称。用于发给对方，对方显示看到。
            //邮箱格式<自己邮箱名>，否则会报错，发送邮箱失败。com.sun.mail.smtp.SMTPSenderFailedException: 553 Mail from must equal authorized user
            String nick = MimeUtility.encodeText("newcoder");//邮件名,昵称，发件人信息。
            InternetAddress from = new InternetAddress(nick+"<uestczhanghuali@163.com>");  //发件地址
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            //发邮件时，也可以应用模板，将一份模板和一些参数，进行渲染。模板不是固定的，人为进行设置。
            // 将handler中的一些model传入进去，那么这份邮件就是自定义的。
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine,template,"UTF-8",model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result,true);
            mailSender.send(mimeMessage);
            return true ;

        }catch (Exception e)
        {
            logger.error("发送邮件失败"+e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();//系统自带的JavaMailSenderImpl的实现。
        mailSender.setUsername("uestczhanghuali@163.com");
        //此处设置登录的密码,这个密码非常重要，它不是邮箱账户的登录密码，而是应用密码，需要登录邮箱，在设置中开启此应用
        //即在邮箱设置中：进行授权，设置客户端授权码:
        //否则很可能出现如下错误：
       //javax.mail.AuthenticationFailedException: 550 User has no permission
        //Authentication failed; nested exception is javax.mail.AuthenticationFailedException: 550 User has no permission



        mailSender.setPassword("自己授权码");



        
        mailSender.setHost("smtp.163.com");  //发送邮件的服务器。
        mailSender.setPort(465);  //使用465端口
        mailSender.setProtocol("smtps");  //协议是smtps，即：要加入ssl
        mailSender.setDefaultEncoding("utf-8");


        System.out.println(mailSender.getUsername());
        System.out.println(mailSender.getPassword());
        System.out.println(mailSender.getHost());
        System.out.println(mailSender.getPort());
        System.out.println(mailSender.getProtocol());
        /**
         * 获得邮件会话属性
         */

        Properties javaMailProperties = new Properties();  //设置一些属性
        //javaMailProperties.put("mail.smtp.host", "smtp.163.com");//指定邮件的发送服务器地址
        //javaMailProperties.put("mail.smtp.auth", true);//服务器是否要验证用户的身份信息
        //javaMailProperties.put("mail.smtp.debug",true);
        mailSender.setJavaMailProperties(javaMailProperties);


    }
}