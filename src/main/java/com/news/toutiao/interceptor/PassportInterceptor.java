package com.news.toutiao.interceptor;

import com.news.toutiao.dao.LoginTicketDAO;
import com.news.toutiao.dao.UserDAO;
import com.news.toutiao.model.HostHolder;
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
    //2.做一个拦截器，在每次访问之前，拦截器插入进来，确定用户是否存在。
    //认证用户是否已经登录
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;



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
            hostHolder.setUser(user);

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null&&hostHolder.getUser()!=null)
        {
            modelAndView.addObject("user",hostHolder.getUser());
            //后端代码和前端交互的地方。
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();

    }
}