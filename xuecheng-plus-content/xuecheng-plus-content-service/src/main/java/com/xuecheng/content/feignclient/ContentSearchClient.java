package com.xuecheng.content.feignclient;

import com.xuecheng.content.feignclient.fallback.SearchServiceClientFallbackFactory;
import com.xuecheng.content.model.po.CourseIndex;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName: ContentSearchClient
 * @Package: com.xuecheng.content.feignclient
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/5 - 16:30
 * @Version: v1.0
 */
@FeignClient(value = "search", fallbackFactory = SearchServiceClientFallbackFactory.class)
public interface ContentSearchClient {
    @ApiOperation("添加课程索引")
    @PostMapping("/search/index/course")
    Boolean add(@RequestBody CourseIndex courseIndex);
}
