package com.news.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * Created by huali on 2018/1/13.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();
    //这里是实现线程安全：
    //在多线程中保证同步，实现线程安全，一个变量要被多个多线程访问，
    // 可以使用threadLocal类来实现线程本地存储的功能。
    //这里每个线程的Thread对象中都有一个ThreadLocalMap对象，
    // 对象存储了一组以ThreadLocal.threadLocalHashCode为键，以本地线程变量为值得k-v值对，
    //ThreadLocal对象就是当前线程的ThreadLocalMap的访问入口，
    // 每个ThreadLocal对象包含一个独一无二的threadLocalHashCode值。
    // 使用这个值可以在线程的K-V值对中找回对应的本地线程变量。

    //ThreadLocal不是为了解决多线程访问共享变量，
    // 而是为每个线程创建一个单独的变量副本，
    // 提供了保持对象的方法和避免参数传递的复杂性。


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