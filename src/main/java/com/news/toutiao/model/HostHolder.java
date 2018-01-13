package com.news.toutiao.model;

/**
 * Created by huali on 2018/1/13.
 */
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();
    //线程本地变量,每条线程都存入自己的User内容

    //1.存储这次访问中，用户是谁。
    //maybe the questioin
    public User getUser()
    {
        return users.get();
    }
    public void setUser(User user)
    {
        users.set(user);
    }
    public void clear()
    {
        users.remove();
    }


}