package com.news.toutiao;

import com.news.toutiao.model.News;
import com.news.toutiao.service.LikeService;
import com.news.toutiao.service.NewsService;
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

    @Autowired
    NewsService newsService;

    //测试的注解
    //测试方法必须使用注解 org.junit.Test 修饰。
    //测试方法必须使用 public void 修饰，而且不能带有任何参数。
    @Test
    public void testLikeA()
    {
        //2.测试
        System.out.println("testLikeA");
        long likecount = likeService.like(14,1,1);
        //News news = newsService.getById(16);
        //int count = news.getLikeCount();
        //newsService.updateLikeCount(16,(int)likecount+count);

        Assert.assertEquals(1,likeService.getLikeStatus(14,1,1));
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

    //@Test:将一个普通方法修饰成一个测试方法
    //@Test(excepted=xx.class): xx.class表示异常类，
    // 表示测试的方法抛出此异常时，认为是正常的测试通过的
    //@Test(timeout=毫秒数) :测试方法执行时间是否符合预期
    @Test(expected = IllegalArgumentException.class)
    public void testException(){
        throw new IllegalArgumentException("xxxx") ;
    }

    //@Before：会在每一个测试方法被运行前执行一次
    @Before
    public void setUp()
    {
        //1.初始化数据
        System.out.println("setUp");


    }
    //@After：会在每一个测试方法运行后被执行一次
    @After
    public void tearDown()
    {
        //4.清理数据
        System.out.println("tearDown");

    }
    //@BeforeClass： 会在所有的方法执行前被执行，static方法
    @BeforeClass
    public static void beforeclass()
    {
        System.out.println("beforeclass");

    }
    //@AfterClass：会在所有的方法执行之后进行执行，static方法
    @AfterClass
    public static void afterclass()
    {
        System.out.println("afterclass");

    }


}