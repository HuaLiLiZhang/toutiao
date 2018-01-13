package com.news.toutiao.interceptor;

import com.news.toutiao.dao.LoginTicketDAO;
import com.news.toutiao.dao.UserDAO;
import com.news.toutiao.model.LoginTicket;
import com.news.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by huali on 2018/1/13.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{
    //认证用户是否已经登录
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;



    @Override  //add the override at the generate
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket =null;
        if(httpServletRequest.getCookies()!=null)
        {
            for (Cookie cookie :httpServletRequest.getCookies())
            {
                if(cookie.getName().equals("ticket"))
                {
                    ticket=cookie.getValue();
                    break;
                }
            }
        }

        if(ticket!=null)
        {
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
            if (loginTicket==null || loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0)
            {
                return true;
            }

            User user=userDAO.selectById(loginTicket.getUserId());
            //httpServletRequest.setAttribute();

        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}