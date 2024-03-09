package com.xuecheng.learning.feignclient.fallback;

import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName: ContentServiceClientFallbackFactory
 * @Package: com.xuecheng.learning.feignclient.fallback
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/9 - 16:14
 * @Version: v1.0
 */
@Slf4j
@Component
public class ContentServiceClientFallbackFactory implements FallbackFactory<ContentServiceClient> {
    @Override
    public ContentServiceClient create(Throwable throwable) {
        return new ContentServiceClient() {
            @Override
            public CoursePublish getCoursepublish(Long courseId) {
                log.error("调用内容管理服务发生熔断:{}", throwable.toString(),throwable);
                return null;
            }
        };
    }
}
