//package com.joker.auth.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@SuppressWarnings("deprecation")
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        // 前面是url路径，后面是视图路径，添加thymeleaf后自动配置prefix为/templates,suffix为.html
//        registry.addViewController("/login").setViewName("forward:/login");
//        registry.addViewController("/home").setViewName("forward:/home");
//        registry.addViewController("/admin").setViewName("/admin");
//    }
//
//}
