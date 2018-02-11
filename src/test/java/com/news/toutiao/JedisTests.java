package com.news.toutiao;

import com.news.toutiao.model.User;
import com.news.toutiao.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huali on 2018/2/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")  //add the init-schema.sql
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;


    @Test
    public void testObject()
    {
        User user=new User();
        user.setHeadUrl("http://images.nowcoder.com/head/100t.png");
        user.setName("user1");
        user.setPassword("pwd");
        user.setSalt("salt");

        jedisAdapter.setObject("user1xxx",user);

        User u=jedisAdapter.getObject("user1xxx",User.class);
        System.out.println(ToStringBuilder.reflectionToString(u));

    }

}