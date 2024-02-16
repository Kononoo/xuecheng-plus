package com.xuecheng.content.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teachplan_work")
@ApiModel(value="TeachplanWork对象", description="")
public class TeachplanWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "作业信息标识")
    @TableField("work_id")
    private Long workId;

    @ApiModelProperty(value = "作业标题")
    @TableField("work_title")
    private String workTitle;

    @ApiModelProperty(value = "课程计划标识")
    @TableField("teachplan_id")
    private Long teachplanId;

    @ApiModelProperty(value = "课程标识")
    @TableField("course_id")
    private Long courseId;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("course_pub_id")
    private Long coursePubId;


}
