package com.news.toutiao.service;

import com.news.toutiao.util.JedisAdapter;
import com.news.toutiao.util.RedisKeyUtil;
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
    //不喜欢的函数
    /**
     * 如果喜欢的话，返回1，不喜欢返回-1，否则返回0
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId,int entityType,int entityId)
    {
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId)))
        {
            return 1;
        }
        //不可达的原因是因为，你前面已经返回不会执行到这一句。
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1:0;
    }

    public long like(int userId,int entityId,int entityType)
    {

        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);

    }

    public long disLike(int userId,int entityId,int entityType)
    {

        //在反对集合里加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        //从喜欢里面删除。
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);

    }



}