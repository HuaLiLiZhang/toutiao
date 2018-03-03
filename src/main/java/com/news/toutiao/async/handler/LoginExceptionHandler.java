package com.news.toutiao.async.handler;

import com.news.toutiao.async.EventHandler;
import com.news.toutiao.async.EventModel;
import com.news.toutiao.async.EventType;
import com.news.toutiao.model.Message;
import com.news.toutiao.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by huali on 2018/3/3.
 */
@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel model) {
        //判断是否有异常登录。
        Message message=new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登录异常");
        message.setFromId(3);//系统发送
        message.setCreatedDate(new Date());
        message.setConversationId("_"+model.getActorId());
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}