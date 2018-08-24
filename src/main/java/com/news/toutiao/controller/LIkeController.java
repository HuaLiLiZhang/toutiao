package com.news.toutiao.controller;

import com.news.toutiao.async.EventModel;
import com.news.toutiao.async.EventProducer;
import com.news.toutiao.async.EventType;
import com.news.toutiao.model.EntityType;
import com.news.toutiao.model.HostHolder;
import com.news.toutiao.model.News;
import com.news.toutiao.service.LikeService;
import com.news.toutiao.service.NewsService;
import com.news.toutiao.util.TouTiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by huali on 2018/2/3.
 */
@Controller
public class LIkeController {
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;//发送一个点赞的事件.


    @RequestMapping(path={"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody

    public String like(@RequestParam ("newsId") int newsId)
    {
        int userId = hostHolder.getUser().getId();
        long likecount = likeService.like(userId, newsId, EntityType.ENTITY_NEWS);

        News news=newsService.getById(newsId);  //点赞点踩的功能是在新闻news里面的字段。
        //所以newservice.updateLikecount。
        newsService.updateLikeCount(newsId,(int) likecount);



        //这里发出点赞事件消息队列，是为了在点赞之后，给被点赞的用户发一条消息。
        //发出like事件时候，把Event事件记录下来，发出去。以后，再在consumer中找到like的handler。
        //然后在调用dohandler，这里like事件发生，事件产生。
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
        .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId)
        .setEntityType(EntityType.ENTITY_NEWS).
                        setEntityOwnerId(news.getUserId()));
        //{"actorId":31,"entityId":21,"entityOwnerId":37,"entityType":1,"exts":{},"type":"LIKE"}
        //actorId :是登录用户的id， entityID是：新闻的id，entityOwnerid：新闻所属用户被点赞用户的id，
        // entityType：实体类型，exts :这里面添加的事件的一些内容，producer的时候才加入。
        return TouTiaoUtil.getJSONString(0,String.valueOf(likecount));//返回到前端，显示新的likecount的数量

    }

    @RequestMapping(path={"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody

    public String disLike(@Param("newsId") int newsId)
    {
        int userId=hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, newsId, EntityType.ENTITY_NEWS);

        //更新喜欢数
        newsService.updateLikeCount(newsId,(int) likeCount);
        return TouTiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

}