package com.news.toutiao.util;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
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
        //print(1,jedis.get("hello"));
        jedis.rename("hello","newhello");
        //print(1,jedis.get("newhello"));
        jedis.setex("hello2",15,"world");//设置过期时间 15s

        //
        jedis.set("pv","100");
        jedis.incr("pv"); //增加1
        //print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        jedis.incrBy("pv",7);
        //print(2,jedis.get("pv"));

        //高并发


        //列表操作
        String listName="listA";
        for (int i=0;i<10;i++)
        {
            jedis.lpush(listName,"a"+String.valueOf(i));
            //从左往右推10个，lpush
        }
      /*  print(3,jedis.lrange(listName,0,12));
        //lrange是提取打印出来，lrange是可以打印从加到几的。
        print(4,jedis.llen(listName));  //获取listname的长度
        print(5,jedis.lpop(listName));  //弹出，从左往右弹出，类似于栈
        print(6,jedis.llen(listName));
        print(3,jedis.lrange(listName,0,12));
        print(7,jedis.lindex(listName,3));  //从左往右数索引
        print(8,jedis.linsert(listName, LIST_POSITION.AFTER,"a4","xx"));
        print(9,jedis.linsert(listName, LIST_POSITION.AFTER,"a4","bb"));
        print(9,jedis.linsert(listName, LIST_POSITION.BEFORE,"a4","aa"));
        //插入到前面即是左边，插入到后面即是右边。
        print(10,jedis.lrange(listName,0,12));
        */


        String userKey="useradc";
        jedis.hset(userKey,"name","jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","475928402349");


        print(1,jedis.hget(userKey,"name"));
        print(2,jedis.hgetAll(userKey));//类似于Map的key,而且是无序的.
        jedis.hdel(userKey,"age");  //删除某个属性
        print(3,jedis.hgetAll(userKey));
        print(4,jedis.hkeys(userKey));
        print(5,jedis.hvals(userKey));
        print(6,jedis.hexists(userKey,"name"));
        print(7,jedis.hexists(userKey,"email"));
        jedis.hsetnx(userKey,"school","uestc");
        jedis.hsetnx(userKey,"age","896");
        jedis.hsetnx(userKey,"name","huali");
        //hsetnx如果存在的话不更新，没有的话，自动补齐。
        print(8,jedis.hgetAll(userKey));

        //set
        String likeKey1="newsLike1";
        String likeKey2="newsLike2";
        for(int i=0;i<10;i++)
        {
            jedis.sadd(likeKey1,String.valueOf(i));
            jedis.sadd(likeKey2,String.valueOf(i*i));

        }
        print(1,jedis.smembers(likeKey1));
        print(2,jedis.smembers(likeKey2));






    }

}