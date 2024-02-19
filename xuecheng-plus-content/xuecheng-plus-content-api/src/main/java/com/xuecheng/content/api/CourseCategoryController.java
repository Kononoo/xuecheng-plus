package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: CourseCategoryController
 * Package: com.xuecheng.content.api
 * Description:
 *  课程分类相关接口
 * @Author: Ronan
 * @Create 2024/2/17 - 0:06
 * @Version: v1.0
 */
@Slf4j
@RestController
@Api(tags = "课程分类管理接口")
@RequestMapping("/course-category")
public class CourseCategoryController {
    @Resource
    private CourseCategoryService courseCategoryService;

    @GetMapping("/tree-nodes")
    @ApiOperation("查询所有课程分类，获取分类树")
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        return courseCategoryService.queryTreeNodes("1");
    }

}
