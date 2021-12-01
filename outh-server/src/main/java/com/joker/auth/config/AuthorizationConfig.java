package com.joker.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;


import javax.sql.DataSource;

/**
 * 授权服务器要做两方面的检验，一方面是校验客户端，另一方面则是校验用户
 */
@Configuration
@EnableAuthorizationServer //表示开启授权服务器自动化配置
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 配置密码加密
     * @return
     */
    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 数据源，用于从数据库获取数据进行认证操作，测试可以从内存中获取
     */
    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenConfig tokenConfig;


    /**
     * jdbc 要求数据库中的表名为:oauth_client_details
     * @return
     */
    public ClientDetailsService clientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }


//    /**
//     * 配置token的基本信息 tokenStore token类型
//     * @return
//     */
//    @Bean
//    AuthorizationServerTokenServices tokenServices(){
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setClientDetailsService(clientDetailsService);
//        tokenServices.setSupportRefreshToken(true);
//        tokenServices.setTokenStore(tokenConfig.tokenStore());
//        tokenServices.setAccessTokenValiditySeconds(60*60*2);
//        tokenServices.setRefreshTokenValiditySeconds(60*60*24*3);
//        return tokenServices;
//    }


    /**
     * 用来配置令牌端点的安全约束、也就是这个端点谁能访问，谁不能访问。
     * 如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
     * 如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的，走basic认证保护
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")// 允许所有人请求令牌
                .checkTokenAccess("permitAll()")//checkTokenAccess 是指一个 Token 校验的端点，这个端点我们设置为可以直接访问（在后面，当资源服务器收到 Token 之后，需要去校验 Token 的合法性，就会访问这个端点）。
                .allowFormAuthenticationForClients();
//        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    public static void main(String[] args) {
        System.out.println(passwordEncoder().encode("secret"));
        System.out.println("$2a$10$y/A37FbRhtLSvAmWfiiMDeC2IIwkjTnjEoXphA.iTJD83mTjNoDMe");
    }

    /**
     * 配置客户端的详细信息、 配置了客户端的 id，secret、资源 id、授权类型、授权范围以及重定向 uri。可在内存或者数据库中
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //基于JDBC
//        clients.withClientDetails(clientDetailsService());
//        //基于内存
        clients.inMemory()
                .withClient("client") //客户端 client_id
                .secret(passwordEncoder().encode("secret")) //客户端 secret
                .authorizedGrantTypes("authorization_code","refresh_token") //授权码类型  授权码，简单，客户端，账户密码
                .scopes("app") //范围
                .redirectUris("http://localhost:8083/login");

//                .and()
//                .withClient("client2") //客户端 client_id
//                .secret(passwordEncoder().encode("secret2")) //客户端 secret
//                .authorizedGrantTypes("authorization_code","refresh_token") //授权码类型  授权码，简单，客户端，账户密码
//                .scopes("all") //范围
//                .redirectUris("http://www.baidu.com");
//                .redirectUris("http://www.baidu.com"); //重定向地址  授权后跳转的地址
    }

    /**
     *  配置令牌的访问端点、与令牌服务. 授权码是用来获取令牌的，使用一次就失效，令牌则是用来获取资源的。
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authorizationCodeServices(authorizationCodeServices())//authorizationCodeService用来配置授权码的存储
//                .tokenServices(tokenServices())//tokenServices用来配置令牌的存储
                .tokenStore(tokenConfig.tokenStore())//JWT令牌存储
//                .userDetailsService(userDetailsService);//用户信息service
                .accessTokenConverter(tokenConfig.accessTokenConverter());
    }



    /**
     * token存储 内存/JDBC
     * @return
     */
    @Bean
    AuthorizationCodeServices authorizationCodeServices(){
        return new InMemoryAuthorizationCodeServices();
    }
}
