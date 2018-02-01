package com.news.toutiao.controller;

import com.news.toutiao.aspect.LogAspect;
import com.news.toutiao.model.User;
import com.news.toutiao.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.print.DocFlavor.STRING;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.*;

/**
 * Created by huali on 2017/12/28.
 */
@Controller  //注解
public class IndexController {
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);


    @Autowired
    private ToutiaoService toutiaoService;


    @RequestMapping(path={"/","/index"})  //接受的是一个URL的地址，访问首页，就用函数处理
    @ResponseBody
    public String  index(HttpSession session )
    {
        logger.info("visit Index!");
        //add logger
        logger.trace("hello ,logger trace");
        return "hello nowCoder,"+session.getAttribute("msg")
                +"<br> Say:"+toutiaoService.say();
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





    @RequestMapping(value={"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderid",defaultValue = "a") String nowcoderId,
                           @RequestParam(value = "key",defaultValue="key") String key,
                           @RequestParam(value = "value",defaultValue="value") String value,
                           HttpServletResponse response)
    {
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "NowcoderId from cookies:"+nowcoderId;
    }





    @RequestMapping("/redirect/{code}")
    public /*RedirectView*/String  redirect(@PathVariable("code") @NumberFormat int code,
                                            HttpSession session)
    {
        /*RedirectView red=new RedirectView("/",true);
        if(code == 301)
        {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;*/

        //return "redirect:/";
        session.setAttribute("msg","Jump from redirect");
        return "redirect:/";

    }




    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required = false)String key)
    {
        if("admin".equals(key))
            return "hello,admin";

        throw new IllegalArgumentException("key error错误");
    }




    //to catch the error and process ,together to deal with it.
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e)
    {
        //Map<String,Object> a=new HashMap<>();
        return "error"+e.getMessage();
    }


}




