package com.news.toutiao.controller;

import com.news.toutiao.aspect.LogAspect;
import com.news.toutiao.model.User;
import com.news.toutiao.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    //初始做页面的时候，只返回一直固定的参数。之后主页面改为home.html自己的主页面，就不返回这个了。
    @RequestMapping(path={"/","/index"})  //接受的是一个URL的地址，访问首页，就用函数处理
    @ResponseBody
    public String  index(HttpSession session )
    {
        logger.info("visit Index!");
        //add logger
        logger.trace("hello ,logger trace");
        //在做重定向的时候，重定向向session中添加了属性SetAttribute("msg","Jump from redirect")
        //重定向之后，会跳转到这个页面。因为重定向返回的URL就是主页。
        //主页显示：
        //hello nowCoder,Jump from redirect
        //Say:this is from toutiaoservice   这里是从service层返回的消息。调用函数嘛。
        return "hello nowCoder,"+session.getAttribute("msg")
                +"<br> Say:"+toutiaoService.say();
    }















    //路径匹配符
    //You can map requests using glob patterns and wildcards:
    //
    //? matches one character
    //
    //* matches zero or more characters within a path segment
    //
    //** match zero or more path segments



    @RequestMapping(value={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue = "1") int type,
                          @RequestParam(value="key",defaultValue="nowcoder") String key)
    {
        //
        return String.format("GID:{%s},UID:{%d},TYPE:{%d},KEY:{%s}",groupId,userId,type,key);
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





    //获取请求get的信息
    @RequestMapping(value={"/request"})
    @ResponseBody
    public String request(HttpServletRequest request/*,
                          HttpServletResponse response,
                          HttpSession session*/)
    {  //获取http浏览器的请求信息，名字和内容，
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




    //返回响应内容，自己可以在返回响应的内容里面加东西，自定义返回内容。
    @RequestMapping(value={"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderid",defaultValue = "a") String nowcoderId,
                           @RequestParam(value = "key",defaultValue="key") String key,
                           @RequestParam(value = "value",defaultValue="value") String value,
                           HttpServletResponse response)
    {
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "NowcoderId from cookies:"+nowcoderId+"  key:"+key+"   value:"+value;
    //    NowcoderId from cookies:23456 key:huali value:23456   nowcoderId其实就等于value的值
    //    通过request获取数据，或者response将更多的数据写回去。
    //    http://127.0.0.1:8080/response?nowcoderid=9347&key=huali&value=23456
    //    Query String Paramters:
    //    nowcoderid: 9347
        //key: huali
        //value: 23456
    }





    @RequestMapping(value = {"/redirect/{code}"})
    public /*RedirectView*/   String  redirect(@PathVariable("code") @NumberFormat int code,
                                            HttpSession session)
    {
        //RedirectView red=new RedirectView("/",true);
        //if(code == 301)
        //{
        //    red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        //}
        //return red;

        session.setAttribute("msg","Jump from redirect");
        //这里可以添加参数，然后我们可以在首页显示出来，就是 index 首页，最前面的index函数
        return "redirect:/";

    }





    @Value("${name}")
    private String n;
    @RequestMapping(value={"/access"})
    @ResponseBody
    public String access()
    {
        return "hello, you have can access ! wellcome "+n;
    }






    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key",required = false)String key)
    {
        //http://127.0.0.1:8080/admin?key=admin
        //页面显示：hello,admin
        if("admin".equals(key))
            return "hello,admin";

        throw new IllegalArgumentException("key error错误 !!! 请重新输入key_");
    }




    //to catch the error and process ,together to deal with it.
    //Spring MVC 外的exception或者spring MVC没有处理的Exception，
    // 就用这种方法进行异常的捕捉。也称为统一处理异常。
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e)
    {
        //Map<String,Object> a=new HashMap<>();
        return "error message is : "+e.getMessage();
    }


}




