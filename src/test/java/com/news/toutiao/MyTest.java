package com.news.toutiao;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * Created by huali on 2018/5/30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)

public class MyTest {
    @Rule
    public OutputCapture outputCapture = new OutputCapture();
    //输出捕捉

    @Test
    public void testname() throws Exception
    {
        System.out.println("hello,world");
        assertThat(outputCapture.toString(), containsString("world"));
    //    断言输出的字符串中是否包含子串world，包含则test成功，不包含则输出错误。
    }
}