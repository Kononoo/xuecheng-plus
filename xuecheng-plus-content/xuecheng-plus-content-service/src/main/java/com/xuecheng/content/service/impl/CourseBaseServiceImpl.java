package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.vo.CourseBaseInfoVo;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.management.QueryEval;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author ronan
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {
    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Resource
    private CourseMarketMapper courseMarketMapper;

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
//        List<CourseBase> list = lambdaQuery()
//                .like(StringUtils.hasText(queryCourseParamsDto.getCourseName()), CourseBase::getName, queryCourseParamsDto.getCourseName())
//                .eq(StringUtils.hasLength(queryCourseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, queryCourseParamsDto.getAuditStatus()).list();

        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称模糊查询,在sql中拼接 course_base.name like '%值%'
        queryWrapper.like(StringUtils.hasText(queryCourseParamsDto.getCourseName()), CourseBase::getName, queryCourseParamsDto.getCourseName());
        //根据课程审核状态查询 course_base.audit_status = ?
        queryWrapper.eq(StringUtils.hasLength(queryCourseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, queryCourseParamsDto.getAuditStatus());

        // 创建page分页查询对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        page = courseBaseMapper.selectPage(page, queryWrapper);

        // 获取数据进行封装
        List<CourseBase> items = page.getRecords();
        long total = page.getTotal();
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, pageParams.getPageNo(), pageParams.getPageSize());
        return courseBasePageResult;
    }

    @Override
    @Transactional
    public CourseBaseInfoVo addCourseBase(Long companyId, AddCourseDto addCourseDto) {
        // 向课程基本信息表 course_base 中写入数据
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto, courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        // 审核状态默认为未提交
        courseBase.setAuditStatus("202002");
        // 发布状态为未发布
        courseBase.setStatus("203001");

        // 插入课程基本数据
        int insert = courseBaseMapper.insert(courseBase);
        if (insert <= 0) {
            throw new RuntimeException("添加课程失败");
        }

        // 向课程营销系course_market写入数据
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto, courseMarket);
        // 添加课程id
        Long courseId = courseBase.getId();
        courseMarket.setId(courseId);
        // 保存营销信息
        saveCourseMarket(courseMarket);
        // 获取课程的详细信息返回
        return getCourseBaseInfo(courseId);
    }

    /**
     * 获取课程返回信息
     * @param courseId 课程id
     * @return 课程信息
     */
    private CourseBaseInfoVo getCourseBaseInfo(Long courseId) {
        // 获取课程基本信息、课程营销信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        // 信息封装
        CourseBaseInfoVo courseBaseInfoVo = new CourseBaseInfoVo();
        BeanUtils.copyProperties(courseBase, courseBaseInfoVo);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoVo);
        }

        // 通过courseCategoryMapper查询分类信息，将分类名称放在courseBaseInfoDto对象, 课程分类的名称设置到courseBaseInfoDto
//        String mtName = courseCategoryMapper.selectById(courseBase.getMt()).getName();
//        String stName = courseCategoryMapper.selectById(courseBase.getSt()).getName();
//        courseBaseInfoVo.setMtName(mtName);
//        courseBaseInfoVo.setStName(stName);
        Map<String, String> nameMap = courseCategoryMapper
                .selectBatchIds(Arrays.asList(courseBase.getMt(), courseBase.getSt())).stream()
                .collect(Collectors.toMap(CourseCategory::getId, CourseCategory::getName));
        courseBaseInfoVo.setMtName(nameMap.get(courseBase.getMt()));
        courseBaseInfoVo.setStName(nameMap.get(courseBase.getSt()));
        return courseBaseInfoVo;
    }

    /**
     * 保存营销信息，逻辑：存在则更新，不存在则添加
     * @param courseMarket 课程营销信息
     */
    private void saveCourseMarket(CourseMarket courseMarket) {
        // 从数据库查询营销信息,存在则更新，不存在则添加
        Long id = courseMarket.getId();
        CourseMarket oldCourseMarket = courseMarketMapper.selectById(id);
        if (oldCourseMarket == null) {
            courseMarketMapper.insert(courseMarket);
        } else {
            // 更新
            BeanUtils.copyProperties(courseMarket, oldCourseMarket);
            courseMarketMapper.updateById(oldCourseMarket);
        }
    }
}
