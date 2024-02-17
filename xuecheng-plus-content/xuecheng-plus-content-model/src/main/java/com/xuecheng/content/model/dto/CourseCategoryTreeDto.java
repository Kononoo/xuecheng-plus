package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: CourseCategoryTreeDto
 * Package: com.xuecheng.content.model.dto
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/17 - 0:04
 * @Version: v1.0
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
    // 子节点
    private List<CourseCategoryTreeDto> childrenTreeNodes;
}
