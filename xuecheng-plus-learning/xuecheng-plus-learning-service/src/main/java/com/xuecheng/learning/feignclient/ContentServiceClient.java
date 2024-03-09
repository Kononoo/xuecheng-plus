package com.xuecheng.learning.feignclient;

import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.fallback.ContentServiceClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: ContentServiceClient
 * @Package: com.xuecheng.learning.feignclient
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/9 - 16:12
 * @Version: v1.0
 */
@FeignClient(value = "content-api", fallbackFactory = ContentServiceClientFallbackFactory.class)
public interface ContentServiceClient {

    @ResponseBody
    @GetMapping("/r/coursepublish/{courseId}")
    CoursePublish getCoursepublish(@PathVariable("courseId") Long courseId)
}
