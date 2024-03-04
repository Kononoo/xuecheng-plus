package com.xuecheng.content.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: CoursePreviewVo
 * @Package: com.xuecheng.content.model.dto
 * @Description:
 *  课程信息预览类：课程基本信息-营销信息-计划信息-师资信息
 * @Author: Ronan
 * @Create 2024/3/4 - 15:01
 * @Version: v1.0
 */
@Data
public class CoursePreviewVo {
    // 课程基本信息、营销信息
    private CourseBaseInfoVo courseBase;
    // 课程计划信息
    private List<TeachplanVo> teachplans;
}
