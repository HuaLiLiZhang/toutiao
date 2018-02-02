package com.news.toutiao.service;

import com.news.toutiao.util.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huali on 2018/2/2.
 */
@Service
public class LikeService {
    //以前写代码的思路：先写模型，然后加入DAO，在DAO内从数据库中读取一些数据。
    //现在思路：不加DAO了，直接使用Jedis方式，使用其中方法读取数据

    @Autowired
    JedisAdapter jedisAdapter;

    //喜欢的函数

    /**
     * 如果喜欢的话，返回1，不喜欢返回-1，否则返回0
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId,int entityType,int entityId)
    {
        PV

    }
    //不喜欢的函数
    //判断喜欢与不喜欢


}