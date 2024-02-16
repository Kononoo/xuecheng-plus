package com.xuecheng.system;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: SystemApplication
 * Package: com.xuecheng.system
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 22:18
 * @Version: v1.0
 */
@EnableSwagger2Doc
@SpringBootApplication
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

}
