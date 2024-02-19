package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: CourseTeacherController
 * Package: com.xuecheng.content.api
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/19 - 16:00
 * @Version: v1.0
 */
@Slf4j
@RestController
@RequestMapping("/courseTeacher")
@Api(value = "教师信息相关接口", tags = "教师信息相关接口")
public class CourseTeacherController {

    @Resource
    private CourseTeacherService courseTeacherService;

    @PostMapping
    @ApiOperation("添加/修改课程教师")
    public CourseTeacher addOrUpdateCourseTeacher(@RequestBody @Validated CourseTeacher courseTeacher) {
        CourseTeacher teacher = courseTeacherService.addOrUpdateCourseTeacher(courseTeacher);
        return teacher;
    }

    @GetMapping("list/{courseId}")
    @ApiOperation("查询教师")
    public List<CourseTeacher> getCourseTeacher(@PathVariable Long courseId) {
        Assert.notNull(courseId, "课程id不能为空");
        List<CourseTeacher> teacherList = courseTeacherService.getCourseTeacherList(courseId);
        return teacherList;
    }

    @ApiOperation("删除教师信息")
    @DeleteMapping("/course/{courseId}/{teacherId}")
    public void deleteCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        Assert.notNull(courseId, "课程id不能为空");
        Assert.notNull(teacherId, "教师id不能为空");
        courseTeacherService.deleteTeacher(courseId, teacherId);
    }
}