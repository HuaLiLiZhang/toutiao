package com.news.toutiao.controller;

import com.news.toutiao.model.News;
import com.news.toutiao.model.ViewObject;
import com.news.toutiao.service.NewsService;
import com.news.toutiao.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huali on 2018/1/9.
 */
@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path={"/reg/"},method={RequestMethod.GET,RequestMethod.POST})
    public String reg(Model model, @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam(value="rember",defaultValue = "0") int remember)
    {
            return username;
    }


}