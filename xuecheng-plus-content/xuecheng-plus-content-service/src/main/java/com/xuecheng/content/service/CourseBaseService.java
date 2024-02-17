package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.dto.UpdateCourseDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.vo.CourseBaseInfoVo;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
public interface CourseBaseService extends IService<CourseBase> {

    /**
     * 课程分页查询
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return 查询结果
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /**
     * 添加课程
     * @param companyId 机构id
     * @param addCourseDto 课程信息
     * @return 详细信息
     */
    CourseBaseInfoVo addCourseBase(Long companyId, AddCourseDto addCourseDto);

    /**
     * 根据课程id查询
     * @param courseId 课程id
     * @return
     */
    CourseBaseInfoVo getCourseBaseInfo(Long courseId);

    /**
     * 根据id修改课程信息
     * @param companyId 机构id
     * @param updateCourseDto 课程信息
     * @return
     */
    CourseBaseInfoVo updateCourseBase(Long companyId, UpdateCourseDto updateCourseDto);
}
