package com.news.toutiao.controller;

import com.news.toutiao.model.News;
import com.news.toutiao.model.ViewObject;
import com.news.toutiao.service.NewsService;
import com.news.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(path={"/","index"},method={RequestMethod.GET,RequestMethod.POST})
    public String index(Model model)
    {
        List<News> newsList=newsService.getLatestNews(0,0,10);
        List<ViewObject> vos=new ArrayList<>();
        for(News news:newsList)
        {
            ViewObject vo=new ViewObject();
            //vo.set("name",news.getId());
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        return "home";
    }


}