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
@TableName("teachplan_media")
@ApiModel(value="TeachplanMedia对象", description="")
public class TeachplanMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "媒资文件id")
    @TableField("media_id")
    private String mediaId;

    @ApiModelProperty(value = "课程计划标识")
    @TableField("teachplan_id")
    private Long teachplanId;

    @ApiModelProperty(value = "课程标识")
    @TableField("course_id")
    private Long courseId;

    @ApiModelProperty(value = "媒资文件原始名称")
    @TableField("media_fileName")
    private String mediaFilename;

    @TableField("create_date")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "创建人")
    @TableField("create_people")
    private String createPeople;

    @ApiModelProperty(value = "修改人")
    @TableField("change_people")
    private String changePeople;


}
