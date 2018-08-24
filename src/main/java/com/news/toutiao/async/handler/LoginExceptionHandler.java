package com.news.toutiao.async.handler;

import com.news.toutiao.async.EventHandler;
import com.news.toutiao.async.EventModel;
import com.news.toutiao.async.EventType;
import com.news.toutiao.model.Message;
import com.news.toutiao.service.MessageService;
import com.news.toutiao.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by huali on 2018/3/3.
 */
@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandler(EventModel model) {
        //判断是否有异常登录。
        Message message=new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登录ip异常");
        message.setFromId(3);//系统发送
        message.setCreatedDate(new Date());
        message.setConversationId("_"+model.getActorId());
        messageService.addMessage(message);
        //前面是对登录异常的用户，发一个消息。



        //                //登录事件的异步队列。
        //                eventProducer.fireEvent(new
        //                        EventModel(EventType.LOGIN).setActorId((int) map.get("userId"))
        //                        .setExt("username", username).setExt("toemail", "1482708264@qq.com"));
        //在登录的事件里面设置了toemail，直接取值就可以知道发给谁的email地址。

        Map<String ,Object>map = new HashMap<String ,Object>();
        map.put("username",model.getExt("username"));   //username也是事件中传入的。

        mailSender.sendWithHTMLTemplate(model.getExt("toemail"),"登录异常",
                "mails/welcome.html",map);
    //    后面是对异常登录的用户发送邮件。
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}