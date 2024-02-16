package com.xuecheng.content.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程发布
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_publish_pre")
@ApiModel(value="CoursePublishPre对象", description="课程发布")
public class CoursePublishPre implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "机构ID")
    @TableField("company_id")
    private Long companyId;

    @ApiModelProperty(value = "公司名称")
    @TableField("company_name")
    private String companyName;

    @ApiModelProperty(value = "课程名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "适用人群")
    @TableField("users")
    private String users;

    @ApiModelProperty(value = "标签")
    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "创建人")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "大分类")
    @TableField("mt")
    private String mt;

    @ApiModelProperty(value = "大分类名称")
    @TableField("mt_name")
    private String mtName;

    @ApiModelProperty(value = "小分类")
    @TableField("st")
    private String st;

    @ApiModelProperty(value = "小分类名称")
    @TableField("st_name")
    private String stName;

    @ApiModelProperty(value = "课程等级")
    @TableField("grade")
    private String grade;

    @ApiModelProperty(value = "教育模式")
    @TableField("teachmode")
    private String teachmode;

    @ApiModelProperty(value = "课程图片")
    @TableField("pic")
    private String pic;

    @ApiModelProperty(value = "课程介绍")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "课程营销信息，json格式")
    @TableField("market")
    private String market;

    @ApiModelProperty(value = "所有课程计划，json格式")
    @TableField("teachplan")
    private String teachplan;

    @ApiModelProperty(value = "教师信息，json格式")
    @TableField("teachers")
    private String teachers;

    @ApiModelProperty(value = "提交时间")
    @TableField("create_date")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "审核时间")
    @TableField("audit_date")
    private LocalDateTime auditDate;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "收费规则，对应数据字典--203")
    @TableField("charge")
    private String charge;

    @ApiModelProperty(value = "现价")
    @TableField("price")
    private Float price;

    @ApiModelProperty(value = "原价")
    @TableField("original_price")
    private Float originalPrice;

    @ApiModelProperty(value = "课程有效期天数")
    @TableField("valid_days")
    private Integer validDays;


}
