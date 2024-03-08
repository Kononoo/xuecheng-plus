package com.xuecheng.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.feignclient.CheckCodeClient;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @ClassName: PasswordAuthServiceImpl
 * @Package: com.xuecheng.ucenter.service.impl
 * @Description:  账号密码方式
 * @Author: Ronan
 * @Create 2024/3/6 - 19:23
 * @Version: v1.0
 */
@Service("password_authservice")
public class PasswordAuthServiceImpl implements AuthService {
    @Resource
    private XcUserMapper xcUserMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private CheckCodeClient checkCodeClient;

    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        // 账号
        String username = authParamsDto.getUsername();

        // 获取验证码，对应的key
        String checkcode = authParamsDto.getCheckcode();
        String checkcodekey = authParamsDto.getCheckcodekey();

        if (StringUtils.isEmpty(checkcode) || StringUtils.isEmpty(checkcodekey)) {
            throw new RuntimeException("请输入验证码");
        }

        // 远程调用验证码服务接口校验验证码
        Boolean verify = checkCodeClient.verify(checkcodekey, checkcode);
        if (verify == null || !verify) {
            throw new RuntimeException("验证码输入错误");
        }

        // 账号是否存在，查询数据库
        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        if (xcUser == null) {
            throw new RuntimeException("账号不存在");
        }

        // 验证密码是否正确
        String password = xcUser.getPassword();
        String passwordDto = authParamsDto.getPassword();
        // 校验密码
        boolean matches = passwordEncoder.matches(passwordDto, password);
        if (!matches) {
            throw new RuntimeException("账号或密码错误");
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);

        return xcUserExt;
    }
}
