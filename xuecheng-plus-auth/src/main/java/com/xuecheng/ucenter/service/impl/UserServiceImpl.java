package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import javafx.beans.property.StringProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: UserServiceImpl
 * @Package: com.xuecheng.ucenter.service.impl
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/6 - 18:01
 * @Version: v1.0
 */
@Slf4j
@Component
public class UserServiceImpl implements UserDetailsService {

    @Resource
    private XcUserMapper xcUserMapper;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 将传入的json转为对象
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = JSON.parseObject(username, AuthParamsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 消息认证类型
        String authType = authParamsDto.getAuthType();

        // 根据认证类型从spring容器中取出指定bean
        String beanName = authType + "_authservice";
        AuthService authService = applicationContext.getBean(beanName, AuthService.class);

        // 调用统一execute方法完成验证
        XcUserExt xcUserExt = authService.execute(authParamsDto);

        // 封装为UserDetails
        UserDetails userDetails = getUserPrinciple(xcUserExt);
        return userDetails;

//        // 根据username账号查询数据库
//        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
//        // 查询用户不存在返回null，Spring Security抛出异常用户不存在
//        if (xcUser == null) {
//            return null;
//        }
//        // 查到用户，拿到密码，封装成UserDetails对象给框架进行比对
//        String password = xcUser.getPassword();
//        String[] authorities = {"test"};
//        UserDetails userDetails = User.withUsername(username).password(password).authorities(authorities).build();
//        return userDetails;
    }

    /**
     * @param xcUserExt 用户id，主键
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     * @description 查询用户信息
     * @author Mr.M
     * @date 2022/9/29 12:19
     */
    private UserDetails getUserPrinciple(XcUserExt xcUserExt) {
        String password = xcUserExt.getPassword();
        // 权限
        String[] authorities = {"test"};
        xcUserExt.setPassword(null);
        // 用户信息转为json
        String userJson = JSON.toJSONString(xcUserExt);
        UserDetails userDetails = User.withUsername(userJson).password(password).authorities(authorities).build();
        return userDetails;
    }
}
