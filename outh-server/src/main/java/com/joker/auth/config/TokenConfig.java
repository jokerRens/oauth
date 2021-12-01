package com.joker.auth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * 提供一个tokenStore实例、指你生成的 Token 要往哪里存储
 * 我们可以存在 Redis 中，也可以存在内存中，也可以结合 JWT 等等
 *
 */

@Configuration
public class TokenConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * 令牌持久化存储接口
     * @return
     */
    @Bean
    TokenStore tokenStore(){

//        return new RedisTokenStore(redisConnectionFactory);
        return new JwtTokenStore(accessTokenConverter());
//        return new InMemoryTokenStore();
    }


    /**
     * jwt令牌转换器 ，对Jwt签名时，增加一个密钥
     * JwtAccessTokenConverter：对Jwt来进行编码以及解码的类
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("test-secret");
        return converter;
    }




}
