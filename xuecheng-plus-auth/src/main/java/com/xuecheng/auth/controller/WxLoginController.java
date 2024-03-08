package com.xuecheng.auth.controller;

import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.WxAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @ClassName: WxLoginController
 * @Package: com.xuecheng.auth.controller
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/7 - 14:47
 * @Version: v1.0
 */
@Slf4j
@Controller
public class WxLoginController {

    @Resource
    private WxAuthService wxAuthService;

    /**
     *  https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
     *  @description 用户允许授权后，将会重定向到redirect_uri的网址上，并且带上code和state参数
     */
    @RequestMapping("/wxLogin")
    public String wxLogin(String code, String state) {

        log.info("微信扫码回调,code:{},state:{}", code, state);
        // 远程调用微信申请令牌，利用令牌获取用户信息
        XcUser xcUser = wxAuthService.wxAuth(code);
        if (xcUser == null) {
            return "redirect:http://www.51xuecheng.cn/error.html";
        }
        String username = xcUser.getUsername();
        return "redirect:hhtp://www.51xuecheng.cn/sign.html?username=" + username + "&authType=wx";
    }
}
