package com.news.toutiao;

import com.news.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huali on 2018/3/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {
    //测试部分
    /**
     * 1.初始化数据
     * 2.执行要测试的业务
     * 3.验证测试的数据
     * 4.清理数据
     */
    @Autowired
    LikeService likeService;

    //测试的注解
    @Test
    public void testLikeA()
    {
        //2.测试
        System.out.println("testLikeA");
        likeService.like(123,1,1);

        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));
        //确认这个一定是这样，否则报错。

    }

    @Test
    public void testDisLikeB()
    {
        //2.测试
        /**
         * setUp
         testLikeA
         tearDown
         setUp
         testLikeB
         tearDown
         */
        System.out.println("testdisLikeB");
        likeService.disLike(123,1,1);
        Assert.assertEquals(-1,likeService.getLikeStatus(123,1,1));


    }

    @Test(expected = IllegalArgumentException.class)
    public void testException(){
        throw new IllegalArgumentException("xxxx") ;
    }

    @Before
    public void setUp()
    {
        //1.初始化数据
        System.out.println("setUp");


    }
    @After
    public void tearDown()
    {
        //4.清理数据
        System.out.println("tearDown");

    }
    @BeforeClass
    public static void beforeclass()
    {
        System.out.println("boforeclass");

    }
    @AfterClass
    public static void afterclass()
    {
        System.out.println("afterclass");

    }


}