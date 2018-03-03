package com.news.toutiao.service;

import com.news.toutiao.controller.IndexController;
import com.news.toutiao.dao.LoginTicketDAO;
import com.news.toutiao.dao.UserDAO;
import com.news.toutiao.model.LoginTicket;
import com.news.toutiao.model.User;
import com.news.toutiao.util.TouTiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by huali on 2018/1/8.
 */
@Service
public class UserService {
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;



    public Map<String ,Object> register(String username, String password)
    {
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username))
        {
            map.put("msgname","用户名不能为空");
            return  map;

        }
        if(StringUtils.isBlank(password))
        {
            map.put("msgpassword","密码不能为空");
            return  map;
        }

        User user=userDAO.selectByName(username);
        if(user!=null)
        {
            map.put("msgname","用户名已经被注册!");
            return  map;
        }

        //密码强度。。。

        user=new User();
        user.setName(username);
        //user.setPassword(password);//不能纯粹保存密码，不安全，需要加密
        user.setSalt(UUID.randomUUID().toString().substring(0,5));//加密工具
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(TouTiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());

        map.put("ticket", ticket);
        return map;

    }

    public Map<String ,Object> login(String username, String password)
    {
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isBlank(username))
        {
            map.put("msgname","用户名不能为空");
            return  map;

        }
        if(StringUtils.isBlank(password))
        {
            map.put("msgpassword","密码不能为空");
            return  map;
        }

        User user=userDAO.selectByName(username);
        if(user==null)
        {
            map.put("msgname","用户名不存在");
            return  map;
        }

        if(!TouTiaoUtil.MD5(password+user.getSalt()).equals(
                user.getPassword()))
        {
            map.put("password","密码不正确");
            return  map;
        }
        //add ticket present you have register
        map.put("userId",user.getId());

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return  map;

        //登录功能


    }

    private String addLoginTicket(int userId)
    {
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date =new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();



    }

    public User getUser(int id)
    {
        return userDAO.selectById(id);
    }

    public void logout(String ticket)
    {
        loginTicketDAO.updateStatus(ticket,1);
    }



}