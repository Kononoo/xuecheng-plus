package com.xuecheng.content.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程分类
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_category")
@ApiModel(value="CourseCategory对象", description="课程分类")
public class CourseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "分类名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "分类标签默认和名称一样")
    @TableField("label")
    private String label;

    @ApiModelProperty(value = "父结点id（第一级的父节点是0，自关联字段id）")
    @TableField("parentid")
    private String parentid;

    @ApiModelProperty(value = "是否显示")
    @TableField("is_show")
    private Integer isShow;

    @ApiModelProperty(value = "排序字段")
    @TableField("orderby")
    private Integer orderby;

    @ApiModelProperty(value = "是否叶子")
    @TableField("is_leaf")
    private Integer isLeaf;


}
