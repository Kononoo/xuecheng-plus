package com.xuecheng.learning.api;

import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.MyCourseTableService;
import com.xuecheng.learning.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Security;

/**
 * @ClassName: MyCourseTableController
 * @Package: com.xuecheng.learning.api
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/9 - 16:34
 * @Version: v1.0
 */
@Slf4j
@Api(value = "我的课程表接口", tags = "我的课程表接口")
@RestController
public class MyCourseTableController {
    @Resource
    private MyCourseTableService myCourseTableService;

    @ApiOperation("添加选课")
    @PostMapping("/choosecourse/{courseId}")
    public XcChooseCourse addChooseCourse(@PathVariable("courseId") Long courseId) {
        // 获取当前用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        if (user == null) {
            throw new LearnOnlineException("请先登录");
        }
        String userId = user.getId();
        // 添加选课
        XcChooseCourse xcChooseCourse = myCourseTableService.addChooseCourse(userId, courseId);
        return xcChooseCourse;
    }

    @ApiOperation("查询学习资格")
    @PostMapping("/choosecourse/learnstatus/{courseId}")
    public XcCourseTablesDto getLearnStatus(@PathVariable("courseId") Long courseId) {
        // 当前登录的用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        if (user == null) {
            throw new LearnOnlineException("请先登录");
        }
        // 用户id
        String userId = user.getId();
        XcCourseTablesDto learningStatus = myCourseTableService.getLearningStatus(userId, courseId);

        return learningStatus;
    }

    @ApiOperation("我的课程表")
    @PostMapping("/mycoursetable")
    public PageResult<XcCourseTables> myCourseTable(MyCourseTableParams params) {
        // 当前登录的用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        if (user == null) {
            throw new LearnOnlineException("请先登录");
        }
        // 用户id
        String userId = user.getId();
        params.setUserId(userId);

        PageResult<XcCourseTables> myCourseTables = myCourseTableService.getMyCourseTables(params);
        return myCourseTables;
    }
}
