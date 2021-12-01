package com.joker.oss.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableOAuth2Sso
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/","/login**")
                .permitAll()
                .anyRequest()
                .authenticated();


//        if("local".equals("da")){
//            http.authorizeRequests().anyRequest().permitAll();
//        }else {
//            http.logout().logoutSuccessUrl("http://localhost:8081/logout")
//                    .and()
//                    .authorizeRequests()
//                    .anyRequest().authenticated()
//                    .and()
//                    .csrf().disable();
//        }
    }
}
