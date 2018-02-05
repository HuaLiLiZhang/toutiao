package com.news.toutiao.controller;

import com.news.toutiao.model.EntityType;
import com.news.toutiao.model.HostHolder;
import com.news.toutiao.model.News;
import com.news.toutiao.model.ViewObject;
import com.news.toutiao.service.LikeService;
import com.news.toutiao.service.NewsService;
import com.news.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huali on 2018/1/9.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;


    private List<ViewObject> getNews(int userId, int offset, int limit)
    {
        List<News> newsList=newsService.getLatestNews(userId,offset,limit);
        int localUserId = hostHolder.getUser()!=null ?hostHolder.getUser().getId():0;
        List<ViewObject> vos=new ArrayList<>();
        for(News news:newsList)
        {
            ViewObject vo=new ViewObject();
            //vo.set("name",news.getId());
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));

            if(localUserId!=0)
            {
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));

            }else {
                vo.set("like",0);
            }

            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path={"/","/index"},method={RequestMethod.GET,RequestMethod.POST})
    public String index(Model model, @RequestParam(value="pop",defaultValue = "0") int pop)
    {                      //这里添加pop，页面再点击登录时，后端传入前端的pop，才会调用弹窗。
        model.addAttribute("vos",getNews(0,0,10));
        if(hostHolder.getUser()!=null)
        {
            pop=0;
        }
        model.addAttribute("pop",pop);//并要把它加入model中
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId,
                            @RequestParam(value = "pop",defaultValue = "0") int pop)
    {
        model.addAttribute("vos",getNews(userId,0,10));
        model.addAttribute("pop",pop);
        return "home";
    }


}