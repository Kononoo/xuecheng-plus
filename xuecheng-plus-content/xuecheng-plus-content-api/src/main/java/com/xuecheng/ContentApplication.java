package com.xuecheng;

import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ClassName: ContentApplication
 * Package: com.xuecheng
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 15:00
 * @Version: v1.0
 */
@Slf4j
@EnableSwagger2Doc
@EnableFeignClients(basePackages = {"com.xuecheng.content.service.jobhandler"})
@SpringBootApplication
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
        log.info("项目启动成功...");
    }
}
