package com.xuecheng.content.model.dto;

import lombok.Data;

/**
 * ClassName: QueryCourseParamsDto
 * Package: com.xuecheng.content.model.dto
 * Description:
 *  查询课程参数模型类
 * @Author: Ronan
 * @Create 2024/2/16 - 15:33
 * @Version: v1.0
 */
@Data
public class QueryCourseParamsDto {
    private String auditStatus;   // 审核状态
    private String courseName;    // 课程名称
    private String publishStatus; // 发布状态
}
