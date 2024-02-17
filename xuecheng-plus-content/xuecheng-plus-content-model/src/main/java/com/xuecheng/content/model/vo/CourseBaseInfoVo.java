package com.xuecheng.content.model.vo;

import com.xuecheng.content.model.po.CourseBase;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ClassName: CourseBaseInfoVo
 * Package: com.xuecheng.content.model.vo
 * Description:
 *  课程基本信息Vo
 * @Author: Ronan
 * @Create 2024/2/17 - 13:35
 * @Version: v1.0
 */
@Data
public class CourseBaseInfoVo extends CourseBase {
    /**
     * 收费规则，对应数据字典
     */
    private String charge;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 咨询qq
     */
    private String qq;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 电话
     */
    private String phone;

    /**
     * 有效期天数
     */
    private Integer validDays;

    /**
     * 大分类名称
     */
    private String mtName;

    /**
     * 小分类名称
     */
    private String stName;
}
