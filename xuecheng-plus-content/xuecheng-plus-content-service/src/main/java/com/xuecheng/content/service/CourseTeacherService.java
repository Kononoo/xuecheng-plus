package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    /**
     * 添加或修改课程教师信息
     *
     * @param courseTeacher
     * @return
     */
    CourseTeacher addOrUpdateCourseTeacher(CourseTeacher courseTeacher);

    /**
     * 根据courseid获取所有教师信息
     * @param courseId
     * @return
     */
    List<CourseTeacher> getCourseTeacherList(Long courseId);

    /**
     * 删除课程教师
     * @param courseId
     * @param teacherId
     */
    void deleteTeacher(Long courseId, Long teacherId);
}
