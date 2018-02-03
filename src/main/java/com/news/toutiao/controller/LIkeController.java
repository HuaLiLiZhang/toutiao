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
    EventProducer eventProducer;


    @RequestMapping(path={"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody

    public String like(@RequestParam ("newsId") int newsId)
    {
        int userId = hostHolder.getUser().getId();
        long likecount = likeService.like(userId, EntityType.ENTITY_NEWS,newsId);

        News news=newsService.getById(userId);
        newsService.updateLikeCount(newsId,(int) likecount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));

        return TouTiaoUtil.getJSONString(0,String.valueOf(likecount));//返回到前端，显示新的likecount的数量

    }

    @RequestMapping(path={"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody

    public String disLike(@Param("newsId") int newsId)
    {
        int userId=hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId,EntityType.ENTITY_NEWS,newsId);

        //更新喜欢数
        newsService.updateLikeCount(newsId,(int) likeCount);
        return TouTiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

}