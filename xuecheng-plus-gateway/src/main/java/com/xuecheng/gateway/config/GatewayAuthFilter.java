package com.xuecheng.gateway.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @ClassName: GatewayAuthFilter
 * @Package: com.xuecheng.gateway.config
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/6 - 17:14
 * @Version: v1.0
 */
@Slf4j
@Component
public class GatewayAuthFilter implements GlobalFilter, Ordered {

    // 白名单
    public static List<String> whiteList = null;
    @Resource
    private TokenStore tokenStore;

    // 加载白名单
    static {
        try (
                InputStream resourceAsStream = GatewayAuthFilter.class.getResourceAsStream("/security-whitelist.properties");
        ) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            Set<String> strings = properties.stringPropertyNames();
            whiteList = new ArrayList<>(strings);
        } catch (IOException e) {
            log.error("加载/security-whitelist.properties出错:{}", e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求的url
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 放行白名单
        for (String url : whiteList) {
            if (pathMatcher.match(url, requestUrl)) {
                return chain.filter(exchange);
            }
        }

        // 检查token是否存在
        String token = this.getToken(exchange);
        if (StringUtils.isEmpty(token)) {
            return buildReturnMono(exchange, "没有认证");
        }

        // 判断token是否有效
        try {
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            boolean expired = oAuth2AccessToken.isExpired();
            if (expired) {
                return buildReturnMono(exchange, "认证令牌已过期");
            }
            return chain.filter(exchange);
        } catch (InvalidTokenException e) {
            log.info("认证令牌无效: {}", token);
            return buildReturnMono(exchange, "认证令牌无效");
        }
    }


    private Mono<Void> buildReturnMono(ServerWebExchange exchange, String error) {
        // 返回响应结果
        ServerHttpResponse response = exchange.getResponse();
        // 添加响应内容
        String jsonString = JSON.toJSONString(new RestErrorResponse(error));
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        // 添加响应头信息
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("content-type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(wrap));
    }


    /**
     * 获取token
     *
     * @param exchange
     * @return
     */
    private String getToken(ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            return null;
        }
        String token = authorization.split(" ")[1];
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return token;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
