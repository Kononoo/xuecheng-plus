package com.xuecheng.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.content.model.vo.CourseBaseInfoVo;
import com.xuecheng.content.model.vo.CoursePreviewVo;
import com.xuecheng.content.model.vo.TeachplanVo;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author ronan
 */
@Slf4j
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {

    @Resource
    private CourseBaseService courseBaseService;
    @Resource
    private CourseBaseMapper courseBaseMapper;
    @Resource
    private TeachplanService teachplanService;
    @Resource
    private CourseMarketMapper courseMarketMapper;
    @Resource
    private CoursePublishPreMapper coursePublishPreMapper;
    @Resource
    private CoursePublishMapper coursePublishMapper;
    @Resource
    private

    @Override
    public CoursePreviewVo getCoursePreviewInfo(Long courseId) {
        CoursePreviewVo coursePreviewVo = new CoursePreviewVo();
        // 获取课程基本信息、营销信息
        CourseBaseInfoVo courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        coursePreviewVo.setCourseBase(courseBaseInfo);
        // 获取课程计划信息
        List<TeachplanVo> teachPlanTree = teachplanService.getTeachPlanTree(courseId);
        coursePreviewVo.setTeachplans(teachPlanTree);

        return coursePreviewVo;
    }

    @Override
    @Transactional
    public void commitAudit(Long companyId, Long courseId) {
        CourseBaseInfoVo courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);
        if (courseBaseInfo == null) {
            throw new LearnOnlineException("找不到课程");
        }
        // 审核状态
        String auditStatus = courseBaseInfo.getAuditStatus();

        // 如果审核状态为已提交则不允许提交
        if ("202003".equals(auditStatus)) {
            throw new LearnOnlineException("课程已提交请等待审核");
        }
        // TODO:本机构只能提交本机构的课程

        // 课程图片、课程信息没有填写也不允许提交
        if (StringUtils.isEmpty(courseBaseInfo.getPic())) {
            throw new LearnOnlineException("请求上传课程图片");
        }

        // 查询课程计划信息
        List<TeachplanVo> teachPlanTree = teachplanService.getTeachPlanTree(courseId);
        if (CollectionUtils.isEmpty(teachPlanTree)) {
            throw new LearnOnlineException("请编写课程计划");
        }

        //查询到课程基本信息、营销信息、计划等信息插入到课程预发布表
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        coursePublishPre.setCompanyId(companyId);
        // 营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        String courseMarketJson = JSON.toJSONString(courseMarket);
        coursePublishPre.setMarket(courseMarketJson);
        // 计划信息
        String teachplanJson = JSON.toJSONString(teachPlanTree);
        coursePublishPre.setTeachplan(teachplanJson);
        // 其他信息
        coursePublishPre.setStatus("202003");
        coursePublishPre.setCreateDate(LocalDateTime.now());

        // 插入或更新课程预发布表
        CoursePublishPre coursePublishPreOne = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPreOne == null) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        // 更新课程基本信息表的审核状态为已提交
        courseBaseMapper.updateAuditStatus(courseId, "202003");
    }

    @Override
    public void publish(Long companyId, Long courseId) {
        // 查询课程发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            throw new LearnOnlineException("课程没有审核记录，无法发布");
        }
        // 课程如果没有审核通过不允许发布
        if (!"202004".equals(coursePublishPre.getStatus())) {
            throw new LearnOnlineException("课程没有审核通过不允许发布");
        }

        // 向课程发布表写入数据
        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        // 先查询课程发布，如果有则更新，没有再添加
        CoursePublish coursePublishOne = coursePublishMapper.selectById(courseId);
        if (coursePublishOne == null) {
            coursePublishMapper.insert(coursePublish);
        } else {
            coursePublishMapper.updateById(coursePublish);
        }

        // 向消息表写入数据
        savaCoursePublishMessage(courseId);

    }

    /**
     * 保存课程消息表信息
     * @param courseId
     */
    private void savaCoursePublishMessage(Long courseId) {

    }
}
