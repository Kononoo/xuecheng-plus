package com.xuecheng.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.exception.CommonError;
import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
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
import com.xuecheng.messagesdk.mapper.MqMessageMapper;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.beans.Encoder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    private MqMessageService mqMessageService;
    @Resource
    private MediaServiceClient mediaServiceClient;


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
        // 其他信息，修改为已审核
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
    @Transactional
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

        // 删除预发布表的数据
        coursePublishPreMapper.deleteById(courseId);
    }

    /**
     * 保存课程消息表信息
     *
     * @param courseId
     */
    private void savaCoursePublishMessage(Long courseId) {
        MqMessage mqMessage = mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
        if (mqMessage == null) {
            throw new LearnOnlineException(CommonError.UNKNOWN_ERROR.getErrMessage());
        }
    }

    @Override
    public File generateCourseHtml(Long courseId) {
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 生成静态文件
        File htmlFile = null;

        try {
            // 拿到模板
            String classpath = this.getClass().getResource("/").getPath();
            configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates"));
            configuration.setDefaultEncoding("utf-8");
            Template template = configuration.getTemplate("course_template.ftl");

            // 获取数据
            CoursePreviewVo coursePreviewInfo = this.getCoursePreviewInfo(courseId);
            HashMap<String, CoursePreviewVo> map = new HashMap<>(1);
            map.put("model", coursePreviewInfo);

            // 装载数据，获取html文件
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
            htmlFile = File.createTempFile("coursePublish", ".html");
            FileOutputStream outputStream = new FileOutputStream(htmlFile);
            int len = -1;
            byte[] buffer = new byte[1024 * 4];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("页面静态化出现问题,课程id:{}", courseId, e);
        }
        return htmlFile;
    }

    @Override
    public void uploadCourseHtml(Long courseId, File file) {
        try {
            // 将File转为MultipartFile
            MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
            // 远程调用得到返回值
            String upload = mediaServiceClient.upload(multipartFile, "course/" + courseId + ".html");
            if (upload == null) {
                log.debug("远程调用走降级逻辑得到上传的结果为null,课程id:{}",courseId);
                throw new LearnOnlineException("上传静态文件过程中存在异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LearnOnlineException("上传静态文件过程中存在异常");
        }

    }
}
