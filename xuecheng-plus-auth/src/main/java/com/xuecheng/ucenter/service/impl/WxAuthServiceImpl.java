package com.xuecheng.ucenter.service.impl;

import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: WxAuthServiceImpl
 * @Package: com.xuecheng.ucenter.service.impl
 * @Description: 微信验证方式
 * @Author: Ronan
 * @Create 2024/3/6 - 19:28
 * @Version: v1.0
 */
@Service("wx_authservice")
public class WxAuthServiceImpl implements AuthService {
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        return null;
    }
}
