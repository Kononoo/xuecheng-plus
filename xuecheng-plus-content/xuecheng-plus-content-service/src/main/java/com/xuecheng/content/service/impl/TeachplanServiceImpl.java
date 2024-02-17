package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.jdbc.jmx.ReplicationGroupManager;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.vo.TeachplanVo;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author ronan
 */
@Slf4j
@Service
public class TeachplanServiceImpl implements TeachplanService {
    @Resource
    private TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanVo> getTeachPlanTree(Long courseId) {
        List<TeachplanVo> teachplanVoList = teachplanMapper.selectTreeNodes(courseId);
        return teachplanVoList;
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        Long id = teachplanDto.getId();
        if (id == null) {
            // 添加业务
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(teachplanDto, teachplan);
            // 确定排序字段，找到它的同级节点个数，排序字段就是个数加1
            Long courseId = teachplanDto.getCourseId();
            Long parentId = teachplanDto.getParentid();
            int count = getTeachPlanCount(courseId, parentId);
            teachplan.setOrderby(count);
            teachplanMapper.insert(teachplan);
        } else {
            // 修改业务
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplan, teachplanDto);
            teachplanMapper.updateById(teachplan);
        }
    }

    private int getTeachPlanCount(Long courseId, Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentId);
        return teachplanMapper.selectCount(queryWrapper) + 1;
    }
}
