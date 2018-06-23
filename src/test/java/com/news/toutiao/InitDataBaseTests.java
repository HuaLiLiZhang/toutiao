package com.news.toutiao;

import com.news.toutiao.dao.CommentDAO;
import com.news.toutiao.dao.LoginTicketDAO;
import com.news.toutiao.dao.NewsDAO;
import com.news.toutiao.model.*;
import com.news.toutiao.dao.UserDAO;
import com.news.toutiao.service.CommentService;
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

	@Autowired
	LoginTicketDAO loginTicketDAO;

	@Autowired
	CommentService commentService;

	@Autowired
	CommentDAO commentDAO;


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
			news.setLink(String.format("http://www.nowcoder.com/%d.html",i));

			newsDAO.addNews(news);


			//test给每一条咨询添加三天咨询
			for(int j=0;j<3;j++)  //每条资讯加三条评论
			{
				Comment comment=new Comment();
				comment.setUserId(i+1);
				comment.setEntityId(news.getId());
				comment.setEntityType(EntityType.ENTITY_NEWS);
				comment.setStatus(0);
				comment.setCreatedDate(new Date());
				comment.setContent("Comment "+String.valueOf(j));
				commentDAO.addComment(comment);
			}



			user.setPassword("newpassword");
			userDAO.updatePassword(user);

			LoginTicket ticket=new LoginTicket();
			ticket.setStatus(0);
			ticket.setUserId(i+1);
			ticket.setExpired(date);
			ticket.setTicket(String.format("TICKET%d",i+1));
			loginTicketDAO.addTicket(ticket);

			loginTicketDAO.updateStatus(ticket.getTicket(),2);



		}
		//public class Assert extends java.lang.Object断言
		//void assertEquals(boolean expected, boolean actual)
		//检查两个变量或者等式是否平衡
		Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());
		userDAO.deleteById(1);
		//void assertNotNull(Object object)
		//检查对象不为空
		//void assertNull(Object object)
		//检查对象为空
		Assert.assertNull(userDAO.selectById(1));
		//Assert.assertNotNull(userDAO.selectById(1));


		Assert.assertEquals(1,loginTicketDAO.selectByTicket("TICKET1").getUserId());
		Assert.assertEquals(2,loginTicketDAO.selectByTicket("TICKET1").getStatus());


		//插入的方法正确，读取的方法正确。取得第一条，肯定不为0，插入正确。
		Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));

	}




}
