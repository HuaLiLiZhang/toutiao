package com.news.toutiao.configuration;

import com.news.toutiao.interceptor.LoginRequiredInterceptor;
import com.news.toutiao.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by huali on 2018/6/19.
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    //未登录跳转，加入拦截器
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    //在java config中，注册拦截器以应用于传入请求

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);  //每次注册时，会回调一遍
        // 注册自定义的拦截器passportInterceptor
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");  //每次注册时，会回调一遍
        //setting界面也设置跳转
        super.addInterceptors(registry);
        //registry.addInterceptor(passportInterceptor)
        //        .addPathPatterns("/api/*") //匹配要过滤的路径
        //        .excludePathPatterns("/api/changePasswordByUser/*") //匹配不过滤的路径。密码还要修改呢，所以这个路径不能拦截
        //        .excludePathPatterns("/api/passwordStateValid") //密码状态验证也不能拦截
        //        .excludePathPatterns("/api/getManagerVersion");//版本信息同样不能拦截

    }
}