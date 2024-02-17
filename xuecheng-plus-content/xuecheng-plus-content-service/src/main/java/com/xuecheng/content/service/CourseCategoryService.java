package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    /**
     * 课程分类树形结构查询
     * @param id
     * @return
     */
    List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
