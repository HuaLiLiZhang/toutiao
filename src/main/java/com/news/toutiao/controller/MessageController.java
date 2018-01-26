package com.news.toutiao.controller;

import com.news.toutiao.model.Message;
import com.news.toutiao.service.MessageService;
import com.news.toutiao.util.TouTiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by huali on 2018/1/26.
 */
@Controller
public class MessageController {
    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(NewsController.class);


    @Autowired
    MessageService messageService;

    @RequestMapping(path = {"/msg/detail"},method = RequestMethod.POST)
    public String conversationDetail(Model model, @Param("conversationId") String conservationId)
    {
        return "letterDetail.html";
    }

    @RequestMapping(path = {"/msg/addMessage"},method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content)
    {
        try {
            Message msg=new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));
            messageService.addMessage(msg);
            return TouTiaoUtil.getJSONString(msg.getId());
        }catch (Exception e)
        {
            logger.error("增加评论失败"+e.getMessage());
            return TouTiaoUtil.getJSONString(1,"插入评论失败");
        }

    }


}