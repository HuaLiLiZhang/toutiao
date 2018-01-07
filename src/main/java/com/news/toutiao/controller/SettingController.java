package com.news.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by huali on 2018/1/4.
 */
@Controller
//Controller is a control layer,in MVC,Unified scheduling of functions in the project
public class SettingController {
    @RequestMapping(value = "/setting")
    @ResponseBody
    public String setting()
    {
        return "Setting ok";
    }
}