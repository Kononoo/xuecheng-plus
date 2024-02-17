package com.xuecheng.content.api;

import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.dto.UpdateCourseDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.vo.CourseBaseInfoVo;
import com.xuecheng.content.service.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * ClassName: CourseBaseInfoController
 * Package: com.xuecheng.content.api
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 15:12
 * @Version: v1.0
 */
@Slf4j
@Api(tags = "课程信息管理接口")
@RequestMapping("/course")
@RestController
public class CourseBaseInfoController {

    @Resource
    private CourseBaseService courseBaseService;

    @ApiOperation("课程分页查询")
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {
        PageResult<CourseBase> courseBasePageResult = courseBaseService.queryCourseBaseList(pageParams, queryCourseParamsDto);
        return courseBasePageResult;
    }

    @ApiOperation("课程添加")
    @PostMapping
    public CourseBaseInfoVo addCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto) {
        // 获取用户所属机构的id
        Long companyId = 1232141425L;
        CourseBaseInfoVo courseBaseInfoVo = courseBaseService.addCourseBase(companyId, addCourseDto);
        return courseBaseInfoVo;
    }

    @ApiOperation("根据课程id查询课程")
    @GetMapping("/{courseId}")
    public CourseBaseInfoVo getCourseById(@PathVariable Long courseId) {
        CourseBaseInfoVo courseBaseInfoVo = courseBaseService.getCourseBaseInfo(courseId);
        return courseBaseInfoVo;
    }

    @ApiOperation("根据id修改课程")
    @PutMapping
    public CourseBaseInfoVo updateCourseBase(@RequestBody @Validated(ValidationGroups.Update.class) UpdateCourseDto updateCourseDto) {
        //获取到用户所属机构的id
        Long companyId = 1232141425L;
        CourseBaseInfoVo courseBaseInfoVo =  courseBaseService.updateCourseBase(companyId, updateCourseDto);
        return courseBaseInfoVo;
    }

}
