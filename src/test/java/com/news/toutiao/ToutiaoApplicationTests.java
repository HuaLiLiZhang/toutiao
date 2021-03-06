package com.news.toutiao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
////在使用所有注释前必须使用@RunWith(SpringJUnit4ClassRunner.class),让测试运行于Spring测试环境
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@WebAppConfiguration
public class ToutiaoApplicationTests {

	@Test
	public void contextLoads() {
	}

}

/*
错误原因是因为，pom文件中版本改为1.3.5以后，在test文件中的测试文件中的SpringBootTest就不能识别：
因为：@SpringBootTest注解是SpringBoot自1.4.0版本开始引入的一个用于测试的注解。

所以要把test文件中的测试代码，改为1.3.5支持的测试代码，头文件，文件包等。

*/

/*import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;  //也就是这行
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToutiaoApplicationTests {

	@Test
	public void contextLoads() {
	}

}*/
