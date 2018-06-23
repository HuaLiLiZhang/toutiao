package com.news.toutiao.service;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * Created by huali on 2018/6/23.
 */
public class myMailSender implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(myMailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;
    public boolean sendWithHTMLTemplate(String to,String subject,
                                        String template,Map<String,Object> model)
    {
    //   HTMl的邮件格式，逐行检验的代码
        try {
            String nick = MimeUtility.encodeText("lalalme");  //发件人昵称
            InternetAddress from = new InternetAddress(nick+"<coureaewr@njwiaewf.com>");  //发件人
            MimeMessage mimeMessage = mailSender.createMimeMessage();  //构建一份邮件
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "UTF8", model);
        //利用velocity的模板引擎，将一份模板传入引擎，model，渲染出来的邮件时自定义的，个性化的。
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;

        }catch (Exception e)
        {
            logger.error("发送邮件失败"+e.getMessage());
            return false;
        }
    }



    @Override    //java系统自带的邮件发送,设置mailsender的邮件对象就设置好了。
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