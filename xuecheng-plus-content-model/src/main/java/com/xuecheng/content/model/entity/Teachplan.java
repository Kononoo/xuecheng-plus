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
 * 课程计划
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teachplan")
@ApiModel(value="Teachplan对象", description="课程计划")
public class Teachplan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "课程计划名称")
    @TableField("pname")
    private String pname;

    @ApiModelProperty(value = "课程计划父级Id")
    @TableField("parentid")
    private Long parentid;

    @ApiModelProperty(value = "层级，分为1、2、3级")
    @TableField("grade")
    private Integer grade;

    @ApiModelProperty(value = "课程类型:1视频、2文档")
    @TableField("media_type")
    private String mediaType;

    @ApiModelProperty(value = "开始直播时间")
    @TableField("start_time")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "章节及课程时介绍")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "时长，单位时:分:秒")
    @TableField("timelength")
    private String timelength;

    @ApiModelProperty(value = "排序字段")
    @TableField("orderby")
    private Integer orderby;

    @ApiModelProperty(value = "课程标识")
    @TableField("course_id")
    private Long courseId;

    @ApiModelProperty(value = "课程发布标识")
    @TableField("course_pub_id")
    private Long coursePubId;

    @ApiModelProperty(value = "状态（1正常  0删除）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "是否支持试学或预览（试看）")
    @TableField("is_preview")
    private String isPreview;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "修改时间")
    @TableField("change_date")
    private LocalDateTime changeDate;


}
