package com.news.toutiao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication  //等同于使用下面三个注解。
//@Configuration
//@ComponentScan
//@EnableAutoConfiguration
public class ToutiaoApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ToutiaoApplication.class);
	}

	public static void main(String[] args) {
		//可以设置进制自动重启程序。
		//System.setProperty("spring.devtools.restart.enabled","false");
		SpringApplication.run(ToutiaoApplication.class, args);
	}
}
