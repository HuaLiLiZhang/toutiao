package com.news.toutiao.util;

import redis.clients.jedis.Jedis;

/**
 * Created by huali on 2018/2/1.
 */
public class JedisAdapter {
    public static void print(int index, Object obj)
    {
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }
    public static void main(String []args)
    {
        Jedis jedis=new Jedis();  //利用Jedis的对象默认连接到本地的127.0.0.1:6379这个端口
        jedis.flushAll();//数据库全删掉，不然会数据冲突。remove all keys from all datasets


        jedis.set("hello","world");  //设置的是以（key,value）类似存储的是Map
        print(1,jedis.get("hello"));
        jedis.rename("hello","newhello");
        print(1,jedis.get("newhello"));
        jedis.setex("hello2",15,"world");//设置过期时间 15s

        //
        jedis.set("pv","100");
        jedis.

    }

}