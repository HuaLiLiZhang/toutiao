package com.news.toutiao.service;

import com.news.toutiao.dao.UserDAO;
import com.news.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huali on 2018/1/8.
 */
@Service
public class UserService {
    @Autowired
    public UserDAO userDAO;

    public User getUser(int id)
    {
        return userDAO.selectById(id);
    }

}