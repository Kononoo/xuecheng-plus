package com.xuecheng.content.model.vo;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * ClassName: TeachplanVo
 * Package: com.xuecheng.content.model.dto
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/17 - 19:26
 * @Version: v1.0
 */
@Data
public class TeachplanVo extends Teachplan {
    // 小章节list
    private List<TeachplanVo> teachPlanTreeNodes;

    // 与媒资管理的信息
    private TeachplanMedia teachplanMedia;
}
