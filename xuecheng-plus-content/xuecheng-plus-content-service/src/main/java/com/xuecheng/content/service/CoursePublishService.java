package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CoursePublish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.vo.CoursePreviewVo;

/**
 * <p>
 * 课程发布 服务类
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
public interface CoursePublishService extends IService<CoursePublish> {

    /**
     * @description 获取课程预览信息
     * @param courseId 课程id
     * @return
     */
    CoursePreviewVo getCoursePreviewInfo(Long courseId);
}
