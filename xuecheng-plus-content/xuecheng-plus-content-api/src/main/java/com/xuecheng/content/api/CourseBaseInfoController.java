package com.xuecheng.content.api;

import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.base.exception.ValidationGroups;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.dto.UpdateCourseDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.vo.CourseBaseInfoVo;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
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
    // 指定权限，有权限才可以访问  字符串套字符串用''号
    @PreAuthorize("hasAuthority('xc_teachmanager_course_list')")
    @PostMapping("/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto queryCourseParamsDto) {
        // 当前登录用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        // 获取用户机构id
        Long companyId;
        if (StringUtils.hasLength(user.getCompanyId())) {
            companyId = Long.valueOf(user.getCompanyId());
        } else {
            throw new LearnOnlineException("无所属机构，没有权限查询！");
        }

        PageResult<CourseBase> courseBasePageResult = courseBaseService.queryCourseBaseList(companyId, pageParams, queryCourseParamsDto);
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
        //获取到用户所属机构的id，这个底层使用了TheadLocal
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        String companyId1 = user.getCompanyId();
        Long companyId = 1232141425L;
        CourseBaseInfoVo courseBaseInfoVo =  courseBaseService.updateCourseBase(companyId, updateCourseDto);
        return courseBaseInfoVo;
    }

    @ApiOperation("根据id删除课程")
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable Long courseId) {
        //获取到用户所属机构的id
        Long companyId = 1232141425L;
        Assert.notNull(courseId, "课程Id不能为空");
        courseBaseService.deleteCourse(companyId, courseId);
    }

}
