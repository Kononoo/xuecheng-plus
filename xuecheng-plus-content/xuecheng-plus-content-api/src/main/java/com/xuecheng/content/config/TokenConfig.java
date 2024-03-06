package com.xuecheng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;

/**
 * @ClassName: TokenConfig
 * @Package: com.xuecheng.config
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/6 - 16:17
 * @Version: v1.0
 */
@Configuration
public class TokenConfig {

    public final String SIGNING_KEY = "mq123";

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);
        return converter;
    }

}
