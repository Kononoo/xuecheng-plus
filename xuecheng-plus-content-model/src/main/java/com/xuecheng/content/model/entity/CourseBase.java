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
 * 课程基本信息
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_base")
@ApiModel(value="CourseBase对象", description="课程基本信息")
public class CourseBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "机构ID")
    @TableField("company_id")
    private Long companyId;

    @ApiModelProperty(value = "机构名称")
    @TableField("company_name")
    private String companyName;

    @ApiModelProperty(value = "课程名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "适用人群")
    @TableField("users")
    private String users;

    @ApiModelProperty(value = "课程标签")
    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "大分类")
    @TableField("mt")
    private String mt;

    @ApiModelProperty(value = "小分类")
    @TableField("st")
    private String st;

    @ApiModelProperty(value = "课程等级")
    @TableField("grade")
    private String grade;

    @ApiModelProperty(value = "教育模式(common普通，record 录播，live直播等）")
    @TableField("teachmode")
    private String teachmode;

    @ApiModelProperty(value = "课程介绍")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "课程图片")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_date")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "修改时间")
    @TableField("change_date")
    private LocalDateTime changeDate;

    @ApiModelProperty(value = "创建人")
    @TableField("create_people")
    private String createPeople;

    @ApiModelProperty(value = "更新人")
    @TableField("change_people")
    private String changePeople;

    @ApiModelProperty(value = "审核状态")
    @TableField("audit_status")
    private String auditStatus;

    @ApiModelProperty(value = "课程发布状态 未发布  已发布 下线")
    @TableField("status")
    private String status;


}
