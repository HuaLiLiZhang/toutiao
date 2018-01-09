package com.news.toutiao;

import com.news.toutiao.dao.NewsDAO;
import com.news.toutiao.model.News;
import com.news.toutiao.model.User;
import com.news.toutiao.dao.UserDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")  //add the init-schema.sql
public class InitDataBaseTests {
	@Autowired
	UserDAO  userDAO;

	@Autowired
	NewsDAO newsDAO;

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

			News news=new News();
			news.setCommentCount(i);
			Date date =new Date();
			date.setTime(date.getTime()+1000*3600*5*i);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
			news.setLikeCount(i+1);
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE{%d}",i));
			news.setLink(String.format("http://www.nowcoder.com",i));

			newsDAO.addNews(news);



			user.setPassword("newpassword");
			userDAO.updatePassword(user);
		}

		Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());
		userDAO.deleteById(1);
		Assert.assertNull(userDAO.selectById(1));



	}

}
