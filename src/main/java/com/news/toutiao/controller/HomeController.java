package com.news.toutiao.controller;

import com.news.toutiao.service.NewsService;
import com.news.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

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
    @ResponseBody
    public String index(HttpSession session)
    {
        return "home";
    }


}