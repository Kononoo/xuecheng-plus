package com.xuecheng.content.mapper;

import com.xuecheng.content.model.po.CourseTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程-教师关系表 Mapper 接口
 * </p>
 *
 * @author ronan
 */
public interface CourseTeacherMapper extends BaseMapper<CourseTeacher> {

    @Delete("delete * from course_teacher where course_id = #{courseId}")
    void deleteByCourseId(@Param("courseId") Long courseId);
}
