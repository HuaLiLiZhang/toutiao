package com.news.toutiao.configuration;

import com.news.toutiao.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by huali on 2018/1/13.
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter{
    //加入拦截器
    @Autowired
    PassportInterceptor passportInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);  //每次注册时，会回调一遍
        super.addInterceptors(registry);
    }
}