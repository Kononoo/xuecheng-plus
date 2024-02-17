package com.xuecheng.content.mapper;

import com.xuecheng.content.model.po.Teachplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.vo.TeachplanVo;
import org.springframework.stereotype.Repository;

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
}
