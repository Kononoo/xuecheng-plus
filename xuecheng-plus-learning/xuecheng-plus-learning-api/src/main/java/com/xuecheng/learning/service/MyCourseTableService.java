package com.xuecheng.learning.service;

import com.xuecheng.learning.model.po.XcChooseCourse;

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
}
