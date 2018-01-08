package com.news.toutiao;

import com.news.toutiao.model.User;
import com.news.toutiao.dao.UserDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")  //add the init-schema.sql
public class InitDataBaseTests {
	@Autowired
	UserDAO  userDAO;

	@Test
	public void initData() {
		for (int i=0;i<11;i++)
		{
			Random random=new Random();
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			user.setPassword("newpassword");
			userDAO.updatePassword(user);
		}

		Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());
		userDAO.deleteById(1);
		Assert.assertNull(userDAO.selectById(1));



	}

}
