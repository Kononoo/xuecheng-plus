package com.xuecheng.content.mapper;

import com.xuecheng.content.model.po.Teachplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.vo.TeachplanVo;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author ronan
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {
    /**
     * 课程计划查询
     * @param courseId
     * @return
     */
    List<TeachplanVo> selectTreeNodes(Long courseId);


    /**
     * 课程计划上排
     * @param courseId
     * @param orderby
     */
    void moveupOrderGrade1(Long courseId, Integer orderby);

    /**
     * 课程小节上排
     * @param parentid
     * @param orderby
     */
    void moveupOrderGrade2(Long parentid, Integer orderby);

    /**
     * 课程计划下排
     * @param courseId
     * @param orderby
     */
    void movedownOrderGrade1(Long courseId, Integer orderby);

    /**
     * 课程小节下排
     * @param parentid
     * @param orderby
     */
    void movedownOrderGrade2(Long courseId, Integer orderby);
}
