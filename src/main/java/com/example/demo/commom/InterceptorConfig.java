package com.example.demo.commom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private LoginInterceptor loginInterceptor;
//
//    List<String> excludes = new ArrayList<String>(){{
//        add("/**/*.html");
//        add("/js/**");
//        add("/css/**");
//        add("/img/**");
////        add("/editor.md/**");
////        add("/user/login");
////        add("/user/reg");
////        add("/art/detail");
////        add("/user/myinfobyuid");
////        add("/art/list");
////        add("/art/totalpage");
//    }};
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration registration = registry.addInterceptor(loginInterceptor);
//        registration.addPathPatterns("/**");
//        registration.excludePathPatterns(excludes);
//    }
//}
