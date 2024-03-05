package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CoursePublish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.vo.CoursePreviewVo;

import java.io.File;

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

    /**
     * 课程发布接口
     * @param companyId
     * @param courseId
     */
    void commitAudit(Long companyId, Long courseId);

    /**
     * 课程发布
     * @param companyId 公司id
     * @param courseId 课程id
     */
    void publish(Long companyId, Long courseId);

    /**
     * 课程静态化
     * @param courseId 课程id
     * @return
     */
    File generateCourseHtml(Long courseId);

    /**
     * 上传课程静态化页面到 Minio
     * @param courseId 课程id
     * @param file 文件
     */
    void uploadCourseHtml(Long courseId, File file);
}
