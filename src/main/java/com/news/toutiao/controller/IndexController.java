package com.news.toutiao.controller;

import com.news.toutiao.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by huali on 2017/12/28.
 */

@Controller  //注解
public class IndexController {
    @RequestMapping(path={"/","/index"})  //接受的是一个URL的地址，访问首页，就用函数处理
    @ResponseBody
    public String  index()
    {
        return "hello nowCoder";
    }

    @RequestMapping(value={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue = "1") int type,
                          @RequestParam(value="key",defaultValue="nowcoder") String key)
    {
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groupId,userId,type,key);
    }


    @RequestMapping(value={"/vm"})
    public String news(Model model)
    {
        model.addAttribute("value1","vvl");
        List<String> colors= Arrays.asList(new String[] {"RED","GREEN","BLUE"});

        Map<String,String> maps=new HashMap<String,String>();
        for(int i=0;i<4;i++)
        {
            maps.put(String.valueOf(i),String.valueOf(i*i));
        }

        model.addAttribute("color",colors);
        model.addAttribute("map",maps);

        model.addAttribute("user",new User("Jim"));    //alt+enter显示可以倒入包的资源

        return "news";
    }

    @RequestMapping(value={"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session)
    {
        StringBuilder sb=new StringBuilder();
        //StringBuffer sb=new StringBuffer();
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements())
        {
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }

        for(Cookie cookie:request.getCookies())
        {
            sb.append("Cookies:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");

        }

        sb.append("getMethod:"+request.getMethod()+"<br>");
        sb.append("getPathInfo:"+request.getPathInfo()+"<br>");
        sb.append("getQueryString:"+request.getQueryString()+"<br>");
        sb.append("getRequestURI:"+request.getRequestURI()+"<br>");



        return sb.toString();

    }














}