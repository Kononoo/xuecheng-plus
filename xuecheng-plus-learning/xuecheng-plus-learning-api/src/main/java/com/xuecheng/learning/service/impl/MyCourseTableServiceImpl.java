package com.xuecheng.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.ContentServiceClient;
import com.xuecheng.learning.mapper.XcChooseCourseMapper;
import com.xuecheng.learning.mapper.XcCourseTablesMapper;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.MyCourseTableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @ClassName: MyCourseTableServiceImpl
 * @Package: com.xuecheng.learning.service.impl
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/9 - 16:36
 * @Version: v1.0
 */
@Slf4j
@Service
public class MyCourseTableServiceImpl implements MyCourseTableService {
    @Resource
    private XcChooseCourseMapper xcChooseCourseMapper;
    @Resource
    private XcCourseTablesMapper xcCourseTablesMapper;
    @Resource
    private ContentServiceClient contentServiceClient;

    @Override
    public XcChooseCourse addChooseCourse(String userId, Long courseId) {
        // 远程调用查询课程是否收费
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        if (coursepublish == null) {
            throw new LearnOnlineException("课程不存在");
        }
        // 收费规则
        String charge = coursepublish.getCharge();
        XcChooseCourse chooseCourse = null;

        // 如果免费则插入选课表和课程表，否则插入选课表
        if ("201000".equals(charge)) {
            // 免费课程
            chooseCourse = addFreeCourse(userId, coursepublish);   // 添加选课表
            XcCourseTables xcCourseTables = addCourseTable(chooseCourse);   // 添加课程表
        } else {
            // 收费课程
            chooseCourse = addChargeCourse(userId, coursepublish);
        }

        // 判断学生的学习资格
        XcCourseTablesDto xcCourseTablesDto = getLearningStatus(userId, courseId);

        // 返回值
        XcChooseCourseDto xcChooseCourseDto = new XcChooseCourseDto();
        BeanUtils.copyProperties(chooseCourse, xcChooseCourseDto);
        xcChooseCourseDto.setLearnStatus(xcCourseTablesDto.getLearnStatus());

        return xcChooseCourseDto;
    }


    /**
     * @description 获取学生的学习资格, 例如：<br>
     *  [{"code":"702001","desc":"正常学习"}<br>{"code":"702002","desc":"没有选课或选课后没有支付"}<br>{"code":"702003","desc":"已过期需要申请续期或重新支付"}]
     * @param userId
     * @param courseId
     * @return
     */
    private XcCourseTablesDto getLearningStatus(String userId, Long courseId) {
        // 构造返回值
        XcCourseTablesDto xcCourseTablesDto = new XcCourseTablesDto();

        // 查询课程表
        XcCourseTables xcCourseTables = this.getXcCourseTables(userId, courseId);
        if (xcCourseTables == null) {
            // 没有选课或没有支付，{"code":"702002","desc":"没有选课或选课后没有支付"}
            xcCourseTablesDto.setLearnStatus("702002");
            return xcCourseTablesDto;
        }

        // 查到数据，判断是否有过期，过期不能继续学习
        boolean expired = xcCourseTables.getValidtimeEnd().isBefore(LocalDateTime.now());
        BeanUtils.copyProperties(xcCourseTables, xcCourseTablesDto);
        if (expired) {
            // "code":"702003","desc":"已过期需要申请续期或重新支付"
            xcCourseTablesDto.setLearnStatus("702003");
        } else {
            // "code":"702001","desc":"正常学习"
            xcCourseTablesDto.setLearnStatus("702001");
        }
        return xcCourseTablesDto;
    }


    /**
     * 添加收费课程 - 插入选课记录表
     *
     * @param userId
     * @param coursePublish
     * @return
     */
    private XcChooseCourse addChargeCourse(String userId, CoursePublish coursePublish) {
        // 课程id
        Long id = coursePublish.getId();
        // 如果存在收费的选课记录
        LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, coursePublish.getId())
                .eq(XcChooseCourse::getOrderType, "700002")   // 收费课程
                .eq(XcChooseCourse::getStatus, "701002");     // 待支付
        XcChooseCourse xcChooseCourse = xcChooseCourseMapper.selectOne(queryWrapper);
        if (xcChooseCourse != null) {
            return xcChooseCourse;
        }

        // 插入选课记录
        xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursePublish.getCompanyId());
        xcChooseCourse.setCourseId(coursePublish.getId());
        xcChooseCourse.setCourseName(coursePublish.getName());
        xcChooseCourse.setOrderType("700002");   // 收费课程
        xcChooseCourse.setCoursePrice(coursePublish.getPrice());
        xcChooseCourse.setValidDays(365);
        xcChooseCourse.setStatus("701002");
        xcChooseCourse.setValidtimeStart(LocalDateTime.now());//有效期的开始时间
        xcChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));//有效期的结束时间

        int insert = xcChooseCourseMapper.insert(xcChooseCourse);
        if (insert <= 0) {
            throw new LearnOnlineException("添加选课记录表失败");
        }
        return xcChooseCourse;
    }


    /**
     * 添加到课程表
     *
     * @param chooseCourse
     * @return
     */
    private XcCourseTables addCourseTable(XcChooseCourse chooseCourse) {
        // 选课成功了才可以添加到课程表
        String status = chooseCourse.getStatus();
        if (!"701001".equals(status)) {
            throw new LearnOnlineException("选课没有成功无法添加到课程表");
        }
        XcCourseTables xcCourseTable = getXcCourseTables(chooseCourse.getUserId(), chooseCourse.getCourseId());
        if (xcCourseTable != null) {
            return xcCourseTable;
        }
        // 添加课程记录
        xcCourseTable = new XcCourseTables();
        BeanUtils.copyProperties(chooseCourse, xcCourseTable);
        xcCourseTable.setChooseCourseId(chooseCourse.getCourseId());
        xcCourseTable.setCourseType(chooseCourse.getOrderType());
        xcCourseTable.setCreateDate(LocalDateTime.now());

        int insert = xcCourseTablesMapper.insert(xcCourseTable);
        if (insert <= 0) {
            throw new LearnOnlineException("添加到课程表失败");
        }
        return xcCourseTable;
    }


    /**
     * 查询课程表记录
     *
     * @param userId
     * @param courseId
     * @return
     */
    private XcCourseTables getXcCourseTables(String userId, Long courseId) {
        LambdaQueryWrapper<XcCourseTables> queryWrapper = new LambdaQueryWrapper<XcCourseTables>()
                .eq(XcCourseTables::getUserId, userId)
                .eq(XcCourseTables::getCourseId, courseId);
        return xcCourseTablesMapper.selectOne(queryWrapper);
    }


    /**
     * 添加免费课程到选课记录表
     *
     * @param userId
     * @param coursePublish
     * @return
     */
    private XcChooseCourse addFreeCourse(String userId, CoursePublish coursePublish) {
        // 课程id
        Long courseId = coursePublish.getId();
        // 如果已存在则返回
        LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getUserId, userId)
                .eq(XcChooseCourse::getCourseId, courseId)
                .eq(XcChooseCourse::getOrderType, "700001")  // 免费课程
                .eq(XcChooseCourse::getStatus, "701001");    // 选课成功
        XcChooseCourse xcChooseCourse = xcChooseCourseMapper.selectOne(queryWrapper);
        if (xcChooseCourse != null) {
            return xcChooseCourse;
        }

        // 向选课表中添加记录
        xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursePublish.getCompanyId());
        xcChooseCourse.setCourseId(courseId);
        xcChooseCourse.setCourseName(coursePublish.getName());
        xcChooseCourse.setCreateDate(LocalDateTime.now());
        xcChooseCourse.setOrderType("700001");  // 免费课程
        xcChooseCourse.setValidDays(365);
        xcChooseCourse.setStatus("701001");     // 选课成功
        xcChooseCourse.setValidtimeStart(LocalDateTime.now());  // 有效期的开始时间
        xcChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365));

        int insert = xcChooseCourseMapper.insert(xcChooseCourse);
        if (insert <= 0) {
            throw new LearnOnlineException("添加选课记录表失败");
        }
        return xcChooseCourse;
    }
}
