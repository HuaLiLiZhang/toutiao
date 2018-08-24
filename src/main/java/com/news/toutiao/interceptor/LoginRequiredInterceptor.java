package com.news.toutiao.interceptor;

import com.news.toutiao.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by huali on 2018/1/13.
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{
    //2.做一个拦截器，判断登陆的拦截器
    //
    @Autowired
    private HostHolder hostHolder;



    @Override  //add the override at the generate
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (hostHolder.getUser()==null)
        {
            httpServletResponse.sendRedirect("/?pop=1");
            //httpServletResponse.sendRedirect("/");
            //设置setting访问跳转
            System.out.println("loginrequriedIntercepter setting!!");
            return false;
        }
        return  true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}