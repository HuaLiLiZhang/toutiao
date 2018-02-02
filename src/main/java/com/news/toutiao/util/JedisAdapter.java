package com.news.toutiao.util;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.*;


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

        //// 数值操作
        jedis.set("pv","100");
        jedis.incr("pv"); //增加1
        print(2,jedis.get("pv"));
        jedis.incrBy("pv",5);
        jedis.incrBy("pv",7);
        print(2,jedis.get("pv"));

        //高并发


        //列表操作,最近来访, 粉丝列表，消息队列
        String listName="listA";
        for (int i=0;i<10;i++)
        {
            jedis.lpush(listName,"a"+String.valueOf(i));
            //从左往右推10个，lpush
        }
        print(3,jedis.lrange(listName,0,12));
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


        // hash, 可变字段
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

        //set  // 集合，点赞用户群, 共同好友
        String likeKey1="newsLike1";
        String likeKey2="newsLike2";
        for(int i=0;i<10;i++)
        {
            jedis.sadd(likeKey1,String.valueOf(i));
            jedis.sadd(likeKey2,String.valueOf(i*i));

        }
        print(1,jedis.smembers(likeKey1));
        print(2,jedis.smembers(likeKey2));
        print(3,jedis.sinter(likeKey1,likeKey2));
        print(4,jedis.sunion(likeKey1,likeKey2));
        print(5,jedis.sdiff(likeKey1,likeKey2));
        print(6,jedis.sismember(likeKey1,"5"));//是否有5这个元素
        jedis.srem(likeKey1,"5");//删除5这个元素。
        print(6,jedis.smembers(likeKey1));
        //打印所有元素，查看likeKey1中没有5这个元素。

        print(7,jedis.scard(likeKey1));   //判断likeKey1中有多少元素，9个
        // 从1移动到2
        jedis.smove(likeKey2,likeKey1,"16");  //将likeKey2中的元素16移动到likeKey1中
        print(8,jedis.scard(likeKey1)); //此时likeKey1中元素为10个，因为加了元素16
        print(9,jedis.smembers(likeKey1));

        // 排序集合，有限队列，排行榜
        String rankKey="rankKey";
        jedis.zadd(rankKey,15,"jim");
        jedis.zadd(rankKey,60,"Ben");
        jedis.zadd(rankKey,90,"Lee");
        jedis.zadd(rankKey,80,"Mei");
        jedis.zadd(rankKey,75,"Lucy");
        print(1,jedis.zcard(rankKey));  //rankKey中有几个元素
        print(2,jedis.zcount(rankKey,60,100));//某个范围内的有几个元素
        print(3,jedis.zscore(rankKey,"Lucy"));  //查找某个用户的分值。

        // 改错卷了
        jedis.zincrby(rankKey,2,"Lucy");  //给Lucy这个人的分数提升 2分 ，变为77分
        print(4,jedis.zscore(rankKey,"Lucy"));
        jedis.zincrby(rankKey,2,"Lucy1");  //rankKey中在没有元素Lucy1的时候，会在提升分数2分时候，自动加入到rankKey中。
        print(5,jedis.zcount(rankKey,0,100));//总共变为6个key,value值。原来为5个
        print(6,jedis.zcard(rankKey));//计算有多少人。key,value 对

        //综合排名
        //输出分数由低到高0-3的名字
        print(7,jedis.zrange(rankKey,0,3));  //输出分数由低到高0-3的名字
        //输出分数由高到低的排名。
        print(8,jedis.zrevrange(rankKey,0,3)); //反向输出0-3的名字。

        // zrangeByScoreWithScores:  返回key的有序集合中的分数在min和max之间的所有元素（包括分数等于max或者min的元素）。元素被认为是从低分到高分排序的。
        //     * 具有相同分数的元素按字典序排列, 指定返回结果的数量及区间。 返回元素和其分数，而不只是元素。
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "0", "100"))
        {
            print(9,tuple.getElement()+":"+String.valueOf(tuple.getScore()));

        }

        //自己的排名，名次也能显示。
        print(10,jedis.zrank(rankKey,"Ben"));//正数排名从低到高
        print(11,jedis.zrevrank(rankKey,"Ben"));//反序排名，从高到底


        //redis是单线程
        JedisPool jedisPool=new JedisPool();
        for(int i=0;i<10;++i)
        {
            Jedis j=jedisPool.getResource();
            j.get("a");
            System.out.println("POOL"+i);
            //线程池，连接池，取出一条资源以后，不放回，而因为pool默认只有8条资源
            //全部被用完了。
            //如果没有close,那么到达第8个就会停住不打印了,程序也不会暂停。
            j.close(); //close的话就是相当于把资源重新return回去。继续打印。

        }

        //实现赞和踩的功能。！！！！！


















    }

}