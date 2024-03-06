package com.xuecheng.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @ClassName: WebSecurityConfig
 * @Package: com.xuecheng.auth.config
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/5 - 20:59
 * @Version: v1.0
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DaoAuthenticationProviderCustom daoAuthenticationProviderCustom;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    //配置用户信息服务
    @Bean
    public UserDetailsService userDetailsService() {
        // 这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("ronan").password("123").authorities("p1").build());
        manager.createUser(User.withUsername("mikasa").password("123").authorities("p2").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() throws IOException {
        // 密码为明文方式
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProviderCustom);
    }

    // 配置安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/r/**").authenticated()   //访问/r开始的请求需要认证通过
                .anyRequest().permitAll()           //其它请求全部放行
                .and()
                .formLogin().successForwardUrl("/login-success");   //登录成功跳转到/login-success
    }

//    public static void main(String[] args) {
//        String password = "111111";
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        for (int i = 0; i < 5; i++) {
//            //生成密码
//            String encode = passwordEncoder.encode(password);
//            System.out.println(encode);
//            //校验密码,参数1是输入的明文 ，参数2是正确密码加密后的串
//            boolean matches = passwordEncoder.matches(password, encode);
//            System.out.println(matches);
//        }
//
//        boolean matches = passwordEncoder.matches("1234", "$2a$10$fb2RlvFwr9HsRu9vH1OxCu/YiMRw6wy5UI6u3s0A.0bVSuR1UqdHK");
//        System.out.println(matches);
//    }

    public static void main(String[] args) {
        String password = "abc123";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 5; i++) {
            // 生成密码
            String encode = bCryptPasswordEncoder.encode(password);
            System.out.println(encode);
            // 校验密码
            boolean matches = bCryptPasswordEncoder.matches(password, encode);
            System.out.println(matches);
        }
    }

}
