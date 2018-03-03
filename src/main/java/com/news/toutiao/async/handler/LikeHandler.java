package com.news.toutiao.async.handler;

import com.news.toutiao.async.EventHandler;
import com.news.toutiao.async.EventModel;
import com.news.toutiao.async.EventType;
import com.news.toutiao.model.Message;
import com.news.toutiao.model.User;
import com.news.toutiao.service.MessageService;
import com.news.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by huali on 2018/2/11.
 */
@Component
public class LikeHandler  implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        //message.setFromId(3);
        message.setFromId(model.getActorId());
        message.setToId(model.getEntityOwnerId());

        User user=userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的资讯，http://127.0.0.1:8080/news/"+model.getEntityId());
        message.setCreatedDate(new Date());

        message.setConversationId(model.getActorId()+"_"+model.getEntityOwnerId());
        messageService.addMessage(message);


    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}