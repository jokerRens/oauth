package com.joker.auth.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebFileterConfig {

    /**
     * 配置过滤器
     * order属性:控制过滤器加载顺序：数字越小，加载越早
     * @return
     */
    @Bean
    public FilterRegistrationBean ValidatorFilterRegistration() {
        //新建过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        // 添加我们写好的过滤器
        registration.setFilter( new TestFilter());
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/*");
        registration.setOrder(Integer.MAX_VALUE-10);
        return registration;
    }

}
