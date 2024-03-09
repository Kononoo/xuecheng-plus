package com.xuecheng.learning.service;

import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;

/**
 * @ClassName: MyCourseTableService
 * @Package: com.xuecheng.learning.service
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/9 - 16:35
 * @Version: v1.0
 */
public interface MyCourseTableService {
    /**
     * @description  添加学生选课
     * @param userId 用户id
     * @param courseId 课程id
     * @return
     */
    XcChooseCourse addChooseCourse(String userId, Long courseId);

    /**
     * @description 判断学习资格
     * @param userId
     * @param courseId
     * @return
     */
    XcCourseTablesDto getLearningStatus(String userId, Long courseId);

    /**
     * @description 我的课程表
     * @param params
     * @return
     */
    PageResult<XcCourseTables> getMyCourseTables(MyCourseTableParams params);
}
