package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.vo.CourseBaseInfoVo;
import com.xuecheng.content.model.vo.CoursePreviewVo;
import com.xuecheng.content.model.vo.TeachplanVo;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author ronan
 */
@Slf4j
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {

    @Resource
    private CourseBaseService courseBaseService;
    @Resource
    private TeachplanService teachplanService;

    @Override
    public CoursePreviewVo getCoursePreviewInfo(Long courseId) {
        CoursePreviewVo coursePreviewVo = new CoursePreviewVo();
        // 获取课程基本信息、营销信息
        CourseBaseInfoVo courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        coursePreviewVo.setCourseBase(courseBaseInfo);
        // 获取课程计划信息
        List<TeachplanVo> teachPlanTree = teachplanService.getTeachPlanTree(courseId);
        coursePreviewVo.setTeachplans(teachPlanTree);

        return coursePreviewVo;
    }
}
