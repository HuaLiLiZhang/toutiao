package com.news.toutiao.service;

import com.news.toutiao.dao.UserDAO;
import com.news.toutiao.model.User;
import com.news.toutiao.util.TouTiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by huali on 2018/1/8.
 */
@Service
public class UserService {
    @Autowired
    public UserDAO userDAO;

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
            map.put("msgname","用户名已经被注册");
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
        return  map;

        //登录功能


}

    public User getUser(int id)
    {
        return userDAO.selectById(id);
    }

}