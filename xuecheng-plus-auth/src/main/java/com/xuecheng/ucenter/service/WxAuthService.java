package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.po.XcUser;

import java.util.Map;

/**
 * @ClassName: WxAuthService
 * @Package: com.xuecheng.ucenter.service
 * @Description: 微信扫码登录
 * @Author: Ronan
 * @Create 2024/3/7 - 16:18
 * @Version: v1.0
 */
public interface WxAuthService {

    /**
     *  微信扫码认证，申请令牌，携带令牌查询用户信息、保存用户信息到数据库
     * @param code 授权码
     * @return
     */
    XcUser wxAuth(String code);

    /**
     * 保存用户数据到数据库
     * @param userInfo
     * @return
     */
    XcUser addWxUser(Map<String, String> userInfo);
}
