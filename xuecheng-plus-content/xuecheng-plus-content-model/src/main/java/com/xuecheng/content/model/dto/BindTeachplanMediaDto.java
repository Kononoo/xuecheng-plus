package com.xuecheng.content.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * ClassName: BindTeachplanMediaDto
 * Package: com.xuecheng.content.model.dto
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/18 - 0:42
 * @Version: v1.0
 */
@Data
@ApiModel(value = "BindTeachplanMediaDto", description = "教学计划-媒资绑定提交数据")
public class BindTeachplanMediaDto {

    @NotBlank(message = "媒资文件id非空")
    @ApiModelProperty(value = "媒资文件id", required = true)
    private String mediaId;

    @ApiModelProperty(value = "媒资文件名称", required = true)
    private String fileName;

    @ApiModelProperty(value = "课程计划标识", required = true)
    private Long teachplanId;
}
