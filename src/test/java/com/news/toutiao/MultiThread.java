package com.news.toutiao;

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
                            System.out.println(String.format("T%d,%d", tid, i));
                        }
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public static void main(String[]argv)
    {
        testThread();
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
                System.out.println(String.format("T_%d,%d", tid, i));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}