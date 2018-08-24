package com.news.toutiao;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huali on 2018/3/4.
 */
public class MultiThread {
    public static void testThread()
    {
        //for (int i=0;i<10;++i)
        //{
        //    new MyThread(i).start();
        //}

        for(int i=0;i<10;++i)
        {
            final int tid=i;
            new Thread(new Runnable()
            {
                @Override
                public void run() {
                    try {
                        for(int i=0;i<10;i++)
                        {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2_%d,%d", tid, i));
                        }
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    private static Object obj = new Object();
    public static void testSynchroniced1()
    {
        synchronized (obj){
            try {
                for(int i=0;i<10;i++)
                {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3_%d,%d",i+1, i));
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchroniced2()
    {
        synchronized (obj){
            try {
                for(int i=0;i<10;i++)
                {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4_%d,%d", i+1,i));
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public static void testSynchroniced3()
    {
        for(int i=0;i<2;++i)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchroniced1();
                    testSynchroniced2();
                }
            }).start();
        }
    }


    public static void testBlockingQueue()
    {
        BlockingQueue<String> q =new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"Consumer1").start();
        new Thread(new Consumer(q),"Consumer2").start();

    }

    public static int counter = 0;//非原子性的操作，++会导致不会加到100，操作错误。
    private static AtomicInteger atomicInteger = new AtomicInteger(0);//原子化操作。
    public static void sleep(int mils)
    {
        try {
            //Thread.sleep(new Random().nextInt(mils));
            Thread.sleep(mils);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static  void testWithAtomic()
    {
        for(int i=0;i<10;++i)
        {
            new Thread((new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for(int j=0;j<10;++j)
                    {
                        System.out.println(atomicInteger.incrementAndGet());
                    }

                }
            })).start();
        }

    }

    public static void testWithoutAtomic(){
        for(int i=0;i<10;++i)
        {
            new Thread((new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for(int j=0;j<10;++j)
                    {
                        counter++;
                        System.out.println(counter);
                    }

                }
            })).start();
        }
    }

    public static void testAtomic(){
        testWithAtomic();  //多线程AtomicInteger，加加的时候，要使用原子性操作。
        testWithoutAtomic();
    }


    private static ThreadLocal<Integer> threadLocalUserId =  new ThreadLocal<>();
    //线程局部变量。即使是一个static成员，每个线程访问的变量是不同的。
    //常见于web中存储当前用户到一个静态工具类中，在线程的任何地方都可以访问到当前线程的用户。
    //HostHolder
    private static int userId;

    public static void testThreadLocal()
    {
        //for(int i = 0;i<10;++i)
        //{
        //    final int finalI=i;
        //    new Thread(new Runnable() {
        //        @Override
        //        public void run() {
        //            threadLocalUserId.set(finalI);
        //            sleep(1000);
        //            System.out.println("ThreadLocal:"+threadLocalUserId.get());
        //        }
        //    }).start();
        //}

        for(int i = 0;i<10;++i)
        {
            final int finalI=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userId = finalI;
                    //如果线程不sleep的话，则会出现userID混乱
                    //NonThreadLocal:1
                    //NonThreadLocal:5
                    //NonThreadLocal:0
                    //NonThreadLocal:4
                    //NonThreadLocal:6
                    //NonThreadLocal:4
                    //NonThreadLocal:3
                    //NonThreadLocal:7
                    //NonThreadLocal:9
                    //NonThreadLocal:8
                    //sleep(1000);
                    //使用sleep，1秒之后，多个线程都已经执行完毕，
                    // 所以都是一个固定的数，6,7,8，9.。。。
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    //NonThreadLocal:6
                    System.out.println("NonThreadLocal:"+Thread.currentThread().getName()+":"+userId);
                }
            }).start();
        }
    }

    //线程池任务框架。
    public static void testExcutor(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        //所有任务都由ExecutorService来执行。只有一个线程
        //executor ：提供一个运行任务的框架。
        //将任务和如何运行任务解耦。
        //用于提供线程池或定时任务服务。
        //ExecutorService service = Executors.newFixedThreadPool(2);
        //所有任务都由ExecutorService来执行。有两个线程，同时进行。多个线程池
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;++i)
                {
                    sleep(1000);
                    System.out.println("Execute1 "+i);
                }

            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;++i)
                {
                    sleep(1000);
                    System.out.println("Execute2 "+i);
                }
            }
        });

        service.shutdown();
        while (!service.isTerminated())
        {
            sleep(1000);
            System.out.println("wait for termination");
        }

    }




    public static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        //所有任务都由ExecutorService来执行。只有一个线程
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                //throw new IllegalArgumentException("异常") ;
                return 100;
            }
        });


        service.shutdown();
        try {
            System.out.println(future.get());
            //System.out.println(future.get(1000,TimeUnit.MILLISECONDS));
            //100毫秒没返回值，就直接抛出异常。
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public static void main(String[]argv)
    {
        //testThread();
        //testSynchroniced3();
        //testBlockingQueue();
        //testAtomic();
        //testThreadLocal();
        testExcutor();
        //testFuture();
    }
}

class MyThread extends Thread
{
    private int tid;
    public MyThread(int tid)
    {
        this.tid=tid;
    }

    @Override
    public void run() {
        super.run();
        try {
            for(int i=0;i<10;i++)
            {
                Thread.sleep(1000);
                System.out.println(String.format("T1_%d,%d", tid, i));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable{
    private BlockingQueue<String> q;
    //同步队列，是线程安全的

    public Producer(BlockingQueue<String> q)
    {
        this.q=q;
    }
    @Override
    public void run() {
        try {
            for(int i=0;i<100;++i)
            {
                Thread.sleep(10);
                q.put(String.valueOf(i));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}


class Consumer implements Runnable{

    private BlockingQueue<String>q;
    //同步队列，是线程安全的。
    public Consumer(BlockingQueue<String> q)
    {
        this.q=q;
    }
    @Override
    public void run() {
        try {
            while (true)
                System.out.println(Thread.currentThread().getName()+":"+q.take());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

