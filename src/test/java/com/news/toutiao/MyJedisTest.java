package com.news.toutiao;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * Created by huali on 2018/6/21.
 */
public class MyJedisTest {
    public static void print(int index, Object obj)
    {
        System.out.println(String.format("%d,%s",index, obj.toString()));
    }

    public static void main(String[]args)
    {
        Jedis jedis = new Jedis();
        jedis.flushAll();//把数据库全部删掉

        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello", "hellowo");
        print(1,jedis.get("hellowo"));
        jedis.setex("hello2",10, "world");

        jedis.set("pv","100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv",5);
        print(3, jedis.get("pv"));
    //   list 列表操作
        String listName= "listA";
        for(int i=0;i<10;i++)
        {
            jedis.lpush(listName, "a"+String.valueOf(i));//从左网右边推进来
        }
        print(3, jedis.lrange(listName, 0,12));
        print(4, jedis.llen(listName));
        print(5, jedis.lpop(listName));
        print(6, jedis.llen(listName));
        print(7, jedis.linsert(listName, LIST_POSITION.AFTER,"a4","xx"));
        print(8, jedis.linsert(listName, LIST_POSITION.BEFORE,"a4","yy"));
        print(9, jedis.lrange(listName, 0,17));


        //hash
        String userkey = "student";
        jedis.hsetnx(userkey, "name","jim");
        jedis.hsetnx(userkey, "age","12");
        jedis.hsetnx(userkey, "phone","134414141235759886");
        print(10, jedis.hget(userkey, "name"));  //HashMap无序
        print(11, jedis.hgetAll(userkey));
        print(12, jedis.hkeys(userkey));
        print(13, jedis.hexists(userkey, "name"));
        print(14, jedis.hexists(userkey, "email"));


    //   集合 set
        String setkey1 = "newslike1";
        String setkey2 = "newslike2";
        for(int i=0;i<10;i++)
        {
            jedis.sadd(setkey1, String.valueOf(i));
            jedis.sadd(setkey2, String.valueOf(i*i));
        }
        print(15, jedis.smembers(setkey1));
        print(16, jedis.smembers(setkey2));

        print(17, jedis.sinter(setkey1, setkey2));
        print(18, jedis.sunion(setkey1,setkey2));
        print(19, jedis.sdiff(setkey1,setkey2));
        print(20,jedis.sismember(setkey1, "5"));
        jedis.srem(setkey1, "5");
        print(21,jedis.sismember(setkey1, "5"));
        print(22,jedis.smembers(setkey1));
        print(23,  jedis.scard(setkey1));

    //有序集合
        String rankKey = "rankey";
        jedis.zadd(rankKey, 43, "jim");
        jedis.zadd(rankKey, 67, "benn");
        jedis.zadd(rankKey, 89, "aer");
        jedis.zadd(rankKey, 12, "cim");

        print(24, jedis.zrange(rankKey,0,8));  //从小到大排序。
        print(25,jedis.zcard(rankKey));
        print(26, jedis.zcount(rankKey, 45,89));
        print(27, jedis.zscore(rankKey, "jim"));
        jedis.zincrby(rankKey,2,"lucy");
        print(28, jedis.zcard(rankKey));
        print(29, jedis.zscore(rankKey,"lucy"));
        print(30, jedis.zrange(rankKey, 0,3));  //从小打到排序，从0开始。
        print(31, jedis.zrevrange(rankKey,0,3));
        print(32, jedis.zrank(rankKey,"jim"));  //判断 自己是第几名


        //牛客网排名。
        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,"0","100"))
        {
            print(33, tuple.getElement()+":"+tuple.getScore());
        }

        JedisPool jedisPool = new JedisPool();
        for(int i=0;i<10;i++)
        {
            Jedis j = jedisPool.getResource();
            j.get("a");
            System.out.println("pool"+i);  //Jedis线程池总共只有八条资源，如果不关闭，则用完了。
            j.close();
        }
        jedis.close();
    }

}