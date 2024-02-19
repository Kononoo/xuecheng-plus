package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.model.vo.TeachplanVo;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private TeachplanMediaMapper teachplanMediaMapper;

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

    @Override
    @Transactional
    public void associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto) {
        Long teachplanId = bindTeachplanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            throw new LearnOnlineException("课程计划不存在");
        }
        // 先删除原有记录,根据课程计划id删除它所绑定的媒资
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getTeachplanId, bindTeachplanMediaDto.getTeachplanId());
        teachplanMediaMapper.delete(queryWrapper);

        // 添加新纪录
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        BeanUtils.copyProperties(bindTeachplanMediaDto, teachplanMedia);
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setMediaFilename(bindTeachplanMediaDto.getFileName());
        teachplanMediaMapper.insert(teachplanMedia);
    }

    @Override
    @Transactional
    public void deleteTeachPlan(Long teachplanId) {
        // 查询当前课程计划是否有小节
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid, teachplanId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new LearnOnlineException("当前课程计划还有小节信息，无发操作");
        }
        // 删除课程计划，及关联的媒资信息
        teachplanMapper.deleteById(teachplanId);
        LambdaUpdateWrapper<TeachplanMedia> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
        teachplanMediaMapper.delete(updateWrapper);
    }

    @Override
    public void orderByTeachplan(String moveType, Long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan == null) {
            throw new LearnOnlineException("当前课程计划不存在");
        }
        Integer grade = teachplan.getGrade();
        Long courseId = teachplan.getCourseId();
        Long parentid = teachplan.getParentid();
        Integer orderby = teachplan.getOrderby();

        if ("moveup".equals(moveType)) {
            if (grade == 1) {
                // 章节上移，找到上一个章节的orderby，然后与其交换orderby
                teachplan.setOrderby(orderby - 1);
                teachplanMapper.updateById(teachplan);
                // 上一个章节向下移
                teachplanMapper.moveupOrderGrade1(courseId, orderby);
            } else if (grade == 2) {
                // 小节上移
                teachplan.setOrderby(orderby - 1);
                teachplanMapper.updateById(teachplan);
                // 上一个小结向下移
                teachplanMapper.moveupOrderGrade2(parentid, orderby);
            }

        } else if ("movedown".equals(moveType)) {
            if (grade == 1) {
                // 章节上移，找到上一个章节的orderby，然后与其交换orderby
                teachplan.setOrderby(orderby + 1);
                teachplanMapper.updateById(teachplan);
                // 上一个章节向下移
                teachplanMapper.moveupOrderGrade1(courseId, orderby);
            } else if (grade == 2) {
                // 小节上移
                teachplan.setOrderby(orderby + 1);
                teachplanMapper.updateById(teachplan);
                // 上一个小结向下移
                teachplanMapper.moveupOrderGrade2(parentid, orderby);
            }
        }

    }
}
