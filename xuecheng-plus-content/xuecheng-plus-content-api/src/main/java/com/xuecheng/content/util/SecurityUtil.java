package com.xuecheng.content.util;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: SecurityUtil
 * @Package: com.xuecheng.content.util
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/6 - 18:38
 * @Version: v1.0
 */
@Slf4j
public class SecurityUtil {

    public static XcUser getUser() {
        try {
            // 拿到当前用户身份
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principalObj instanceof String) {
                // 获取用户身份信息
                String principal = principalObj.toString();
                // 转成json对象
                XcUser xcUser = JSON.parseObject(principal, XcUser.class);
                return xcUser;
            }
        } catch (Exception e) {
            log.error("获取当前登录用户身份出错:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    @Data
    public static class XcUser implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id;

        private String username;

        private String password;

        private String salt;

        private String name;
        private String nickname;
        private String wxUnionid;
        private String companyId;
        /**
         * 头像
         */
        private String userpic;

        private String utype;

        private LocalDateTime birthday;

        private String sex;

        private String email;

        private String cellphone;

        private String qq;

        /**
         * 用户状态
         */
        private String status;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
    }
}
