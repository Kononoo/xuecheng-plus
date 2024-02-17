package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * ClassName: AddCourseDto
 * Package: com.xuecheng.content.model.dto
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/17 - 13:56
 * @Version: v1.0
 */
@Data
@ApiModel(value = "AddCourseDto", description = "新增课程基本信息")
public class AddCourseDto {

    @NotBlank(message = "课程名称不能为空")
    @ApiModelProperty(value = "课程名称", required = true)
    private String name;

    @NotBlank(message = "适用人群不能为空")
    @Size(min = 10, message = "适用人群内容过少")
    @ApiModelProperty(value = "适用人群", required = true)
    private String users;

    @ApiModelProperty("课程名称")
    private String tags;

    @NotBlank(message = "课程大分类不能为空")
    @ApiModelProperty(value = "大分类", required = true)
    private String mt;

    @NotBlank(message = "课程小分类不能为空")
    @ApiModelProperty(value = "小分类", required = true)
    private String st;

    @NotBlank(message = "课程等级不能为空")
    @ApiModelProperty(value = "课程等级", required = true)
    private String grade;

    @ApiModelProperty(value = "教学模式（普通，录播，直播等）", required = true)
    private String teachmode;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "课程图片", required = true)
    private String pic;

    @NotEmpty(message = "收费规则不能为空")
    @ApiModelProperty(value = "收费规则，对应数据字典", required = true)
    private String charge;

    @ApiModelProperty(value = "价格")
    private Float price;
    @ApiModelProperty(value = "原价")
    private Float originalPrice;

    @ApiModelProperty(value = "qq")
    private String qq;

    @ApiModelProperty(value = "微信")
    private String wechat;
    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "有效期")
    private Integer validDays;
}
