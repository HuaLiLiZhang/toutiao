package com.news.toutiao.service;

import com.news.toutiao.dao.MessageDAO;
import com.news.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huali on 2018/1/26.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    public int addMessage(Message message)
    {
        return messageDAO.addMessage(message);
    }

    public List<Message> getConservationDetail(String conversationId,int offset,int limit)
    {
        //System.out.println(messageDAO.getConservationDetail(conversationId,offset,limit));
        return messageDAO.getConservationDetail(conversationId,offset,limit);
    }


    public List<Message> getConversationList(int userId,int offset,int limit)
    {
        return messageDAO.getConversationList(userId,offset,limit);
    }
    public int getConversationUnreadCount(int userId,String conversationId)
    {
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }
}

