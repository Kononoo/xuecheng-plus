package com.xuecheng.learning.api;

import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.service.MyCourseTableService;
import com.xuecheng.learning.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}
