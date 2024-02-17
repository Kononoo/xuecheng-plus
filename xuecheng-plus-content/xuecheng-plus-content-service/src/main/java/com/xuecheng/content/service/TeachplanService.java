package com.xuecheng.content.service;

import com.xuecheng.content.model.po.Teachplan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.vo.TeachplanVo;

import java.util.List;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *  课程计划管理相关接口
 * @author ronan
 * @since 2024-02-16
 */
public interface TeachplanService {

    /**
     * 根据课程 id 查询课程计划
     * @param courseId 课程计划
     * @return
     */
    List<TeachplanVo> getTeachPlanTree(Long courseId);
}
