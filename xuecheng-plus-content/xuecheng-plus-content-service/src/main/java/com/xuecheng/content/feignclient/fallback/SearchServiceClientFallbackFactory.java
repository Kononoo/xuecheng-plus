package com.xuecheng.content.feignclient.fallback;

import com.xuecheng.content.feignclient.ContentSearchClient;
import com.xuecheng.content.model.po.CourseIndex;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SearchServiceClientFallbackFactory
 * @Package: com.xuecheng.content.feignclient.fallback
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/5 - 16:34
 * @Version: v1.0
 */
@Slf4j
@Component
public class SearchServiceClientFallbackFactory implements FallbackFactory<ContentSearchClient> {
    @Override
    public ContentSearchClient create(Throwable throwable) {
        return new ContentSearchClient() {
            @Override
            public Boolean add(CourseIndex courseIndex) {
                log.error("添加课程索引发生熔断,索引信息:{},熔断异常:{}",courseIndex,throwable.toString(),throwable);
                // 熔断降级 false
                return false;
            }
        };
    }
}
