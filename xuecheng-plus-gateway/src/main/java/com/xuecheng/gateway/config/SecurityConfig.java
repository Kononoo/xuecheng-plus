package com.xuecheng.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @ClassName: SecurityConfig
 * @Package: com.xuecheng.gateway.config
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/6 - 17:09
 * @Version: v1.0
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // 安全拦截配置
    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity.authorizeExchange()
                .pathMatchers("/**").permitAll()
                .anyExchange().authenticated()
                .and().csrf().disable().build();
    }
}
