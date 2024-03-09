package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.netflix.ribbon.Ribbon;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.mapper.XcUserRoleMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.model.po.XcUserRole;
import com.xuecheng.ucenter.service.AuthService;
import com.xuecheng.ucenter.service.WxAuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @ClassName: WxAuthServiceImpl
 * @Package: com.xuecheng.ucenter.service.impl
 * @Description: 微信验证方式
 * @Author: Ronan
 * @Create 2024/3/6 - 19:28
 * @Version: v1.0
 */
@Service("wx_authservice")
public class WxAuthServiceImpl implements AuthService, WxAuthService {

    @Resource
    private XcUserMapper xcUserMapper;
    @Resource
    private XcUserRoleMapper xcUserRoleMapper;
    @Resource
    private WxAuthService currentProxy;
    @Resource
    private RestTemplate restTemplate;

    @Value("${weixin.appid}")
    private String appid;
    @Value("${weixin.secret}")
    private String secret;

    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        // 获取账号
        String username = authParamsDto.getUsername();
        // 查询数据库
        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        if (xcUser == null) {
            throw new RuntimeException("用户不存在");
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);
        return xcUserExt;
    }


    @Override
    public XcUser wxAuth(String code) {
        // 申请令牌
        Map<String, String> accessTokenMap = getAccessToken(code);
        // 获取令牌权限
        String accessToken = accessTokenMap.get("access_token");
        String openid = accessTokenMap.get("openid");  // openid 授权用户唯一标识  unionid 当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
        // 携带令牌访问用户信息
        Map<String, String> userInfo = getUserInfo(accessToken, openid);

        // 保存用户数据
        XcUser xcUser = currentProxy.addWxUser(userInfo);

        return xcUser;
    }

    @Override
    @Transactional
    public XcUser addWxUser(Map<String, String> userInfo) {
        String unionid = userInfo.get("unionid");
        String nickname = userInfo.get("nickname");
        // 根据unionid获取用户信息
        XcUser xcUser = xcUserMapper.selectOne(new LambdaQueryWrapper<XcUser>().eq(XcUser::getWxUnionid, unionid));
        if (xcUser != null) {
            return xcUser;
        }
        // 向用户表添加记录
        xcUser = new XcUser();
        String userId = UUID.randomUUID().toString();
        xcUser.setId(userId);
        xcUser.setUsername(unionid);
        xcUser.setPassword(unionid);
        xcUser.setWxUnionid(unionid);
        xcUser.setNickname(nickname);
        xcUser.setUtype("101001");  // 学生类型
        xcUser.setStatus("1");      // 用户状态
        xcUser.setCreateTime(LocalDateTime.now());
        xcUserMapper.insert(xcUser);

        // 向用户关系表中添加记录
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setId(UUID.randomUUID().toString());
        xcUserRole.setUserId(userId);
        xcUserRole.setRoleId("17");  // 学生角色
        xcUserRole.setCreateTime(LocalDateTime.now());
        xcUserRoleMapper.insert(xcUserRole);

        return xcUser;
    }

    /**
     * 携带令牌查询用户信息
     *
     * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     *
     * {
     * "openid":"OPENID",
     * "nickname":"NICKNAME",
     * "sex":1,
     * "province":"PROVINCE",
     * "city":"CITY",
     * "country":"COUNTRY",
     * "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
     * "privilege":[
     * "PRIVILEGE1",
     * "PRIVILEGE2"
     * ],
     * "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
     *
     * }
     * @param accessToken
     * @param openid
     * @return
     */
    private Map<String, String> getUserInfo(String accessToken, String openid) {
        String urlTemplate = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        String url = String.format(urlTemplate, accessToken, openid);
        // Remote call url
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        // Get the result
        String result = new String(Objects.requireNonNull(exchange.getBody()).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        Map<String, String> map = JSON.parseObject(result, Map.class);
        return map;
    }


    /**
     * 携带授权码申请令牌
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     *
     * {
     * "access_token":"ACCESS_TOKEN",
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     * @param code 授权
     * @return
     */
    private Map<String, String> getAccessToken(String code) {
        String url_template = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        // final request url
        String url = String.format(url_template, appid, secret, code);

        // remote call url
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        // receive the result
        String result = exchange.getBody();
        // convert result to map
        Map<String, String> map = JSON.parseObject(result, Map.class);
        return map;
    }


}
