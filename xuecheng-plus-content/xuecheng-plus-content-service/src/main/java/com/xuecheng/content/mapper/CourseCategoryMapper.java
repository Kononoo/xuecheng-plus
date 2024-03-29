package com.xuecheng.content.mapper;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author ronan
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    // 递归查询分类信息
    List<CourseCategoryTreeDto> selectTreeNodes(String id);
}
