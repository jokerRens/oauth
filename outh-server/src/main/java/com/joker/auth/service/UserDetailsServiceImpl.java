package com.joker.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;


/**
 * 自定义授权认证实现类
 */

@Service
@Primary //标识为主要实现类，自动注入会优先选择此实现类
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        if(username == null){
            return null;
        }

        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpBasic认证，httpBasic中存储了client_id 和 client_secret，开始认证 client_id 和 client_secret
        if(authentication == null){
            // AuthorizationServerConfig.java 配置了 ClientDetails 的实现方式为 JdbcClientDetailsService
            // 查看 JdbcClientDetailsService 源码可知其提供了客户端凭证的 增删改查 方法，这里主要使用了 根据用户名查找的方式
            try {
                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
                if(clientDetails!=null){
                    //秘钥
                    String clientSecret = clientDetails.getClientSecret();
                    // 通过 客户端id和密钥访问系统：静态方式就是密钥未 BCryptPasswordEncoder() 加密编码
                    //静态方式：return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                    return new User(username,new BCryptPasswordEncoder().encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                }
            }catch (ClientRegistrationException e){
                System.out.println(String.format("客户端ID %s 不存在！", username));
                throw new UsernameNotFoundException(String.format("客户端ID %s 不存在！", username));
            }
        }
        return null;
    }
}
