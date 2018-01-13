package com.news.toutiao.controller;

import com.news.toutiao.model.News;
import com.news.toutiao.model.ViewObject;
import com.news.toutiao.service.NewsService;
import com.news.toutiao.service.UserService;
import com.news.toutiao.util.TouTiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huali on 2018/1/9.
 */
@Controller
public class LoginController {
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path={"/reg/"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remember,
                      HttpServletResponse response)
    {
            try{
                Map<String,Object> map=userService.register(username,password);
                if (map.containsKey("ticket")) {
                    Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                    cookie.setPath("/");
                    if(remember>0)
                    {
                        cookie.setMaxAge(3600*24*5);
                    }
                    response.addCookie(cookie);

                    return TouTiaoUtil.getJSONString(0, "注册成功");//map为空代表注册成功，因为加到User中了
                }else {
                    return TouTiaoUtil.getJSONString(1, map);
                }//{"code":0,"msg":"dfsdg"}, <b>xml</b> json数据格式

            }

            catch (Exception e)
            {
                logger.error("注册异常"+e.getMessage());
                return TouTiaoUtil.getJSONString(1,"注册异常");
            }
    }


    @RequestMapping(path={"/login/"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remember)
    {
        try{
            Map<String,Object> map=userService.register(username,password);
            if (map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0)
                {
                    cookie.setMaxAge(3600*24*5);
                }
                return TouTiaoUtil.getJSONString(0, "注册成功");//map为空代表注册成功，因为加到User中了
            }else {
                return TouTiaoUtil.getJSONString(1, map);
            }
//{"code":0,"msg":"dfsdg"}, <b>xml</b> json数据格式
        }

        catch (Exception e)
        {
            logger.error("注册异常"+e.getMessage());
            return TouTiaoUtil.getJSONString(1,"注册异常");
        }
    }


}