package com.news.toutiao.controller;

import com.news.toutiao.model.HostHolder;
import com.news.toutiao.model.Message;
import com.news.toutiao.model.User;
import com.news.toutiao.model.ViewObject;
import com.news.toutiao.service.MessageService;
import com.news.toutiao.service.UserService;
import com.news.toutiao.util.TouTiaoUtil;
import org.apache.commons.validator.Msg;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huali on 2018/1/26.
 */
@Controller
public class MessageController {
    private static final org.slf4j.Logger logger= LoggerFactory.getLogger(NewsController.class);


    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String conversationDetail(Model model)
    {
        try {
            int localUserId=hostHolder.getUser().getId();
            List<ViewObject> conversations=new ArrayList<ViewObject>();
            List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
            for(Message msg:conversationList)
            {
                ViewObject vo=new ViewObject();
                vo.set("conversation", msg);
                int targetId=msg.getFromId()==localUserId?msg.getToId():msg.getFromId();
                User user=userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,msg.getConversationId()));
                conversations.add(vo);

            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e)
        {
            logger.error("获取站内信列表失败",e.getMessage());
        }
        return "letter";

    }



    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId)
    {
        try {
            List<Message> conversationList = messageService.getConservationDetail(conversationId, 0, 10);
            List<ViewObject> messages=new ArrayList<>();
            for (Message msg:conversationList)
            {
                ViewObject vo=new ViewObject();
                vo.set("message",msg);
                User user=userService.getUser(msg.getFromId());
                if(user==null)
                {
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);

            }
            model.addAttribute("message",messages);

        }catch (Exception e )
        {
            logger.error("获取详情消息失败",e.getMessage());
        }
        return "letterDetail";
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