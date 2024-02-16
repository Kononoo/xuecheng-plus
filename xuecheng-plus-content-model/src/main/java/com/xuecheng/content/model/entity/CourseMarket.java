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
 * 课程营销信息
 * </p>
 *
 * @author ronan
 * @since 2024-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_market")
@ApiModel(value="CourseMarket对象", description="课程营销信息")
public class CourseMarket implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，课程id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "收费规则，对应数据字典")
    @TableField("charge")
    private String charge;

    @ApiModelProperty(value = "现价")
    @TableField("price")
    private Float price;

    @ApiModelProperty(value = "原价")
    @TableField("original_price")
    private Float originalPrice;

    @ApiModelProperty(value = "咨询qq")
    @TableField("qq")
    private String qq;

    @ApiModelProperty(value = "微信")
    @TableField("wechat")
    private String wechat;

    @ApiModelProperty(value = "电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "有效期天数")
    @TableField("valid_days")
    private Integer validDays;


}
