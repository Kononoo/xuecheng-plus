package com.xuecheng.content.service.jobhandler;

import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.File;

/**
 * @ClassName: CoursePublishTask
 * @Package: com.xuecheng.content.service.jobhandler
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/4 - 21:19
 * @Version: v1.0
 */
@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

    @Resource
    private CoursePublishService coursePublishService;
    @Resource
    private CoursePublishMapper coursePublishMapper;

    @XxlJob("CoursePublishJobHandler")
    public void coursePublishJobHandler() {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();  // 执行器的序号，come from no1
        int shardTotal = XxlJobHelper.getShardTotal();  // 执行器总数
        // 多线程执行任务
        process(shardIndex, shardTotal, "course_publish", 30, 60);
    }

    @Override
    public boolean execute(MqMessage mqMessage) {
        // 从mqMessage拿到数据
        Long courseId = Long.valueOf(mqMessage.getBusinessKey1());

        // 课程静态化上传到minio
        generateCourseHtml(mqMessage, courseId);

        // 向elasticsearch写索引数据
        saveCourseIndex(mqMessage, courseId);
        // 开启redis缓存

        // 课程静态化传到nginx

        // 返回true表示任务完成
        return true;
    }




    /**
     * 生成课程静态化页面上传到文件系统
     * @param mqMessage
     * @param courseId
     */
    private void generateCourseHtml(MqMessage mqMessage, Long courseId) {
        // 做任务做幂等性校验
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        int stageOne = mqMessageService.getStageOne(taskId);
        if (stageOne == 1) {
            log.debug("课程静态化任务完成，无需处理...");
            return;
        }
        // 开始进行课程静态化，生成Html页面
        File file = coursePublishService.generateCourseHtml(courseId);
        if (file == null) {
            throw new LearnOnlineException("生成的静态页面为空");
        }
        // 将html页面上传到minio
        coursePublishService.uploadCourseHtml(courseId, file);

        // 任务处理完成
        mqMessageService.completedStageOne(taskId);
    }

    /**
     * 保存课程索引信息，第二个阶段任务
     * @param mqMessage
     * @param courseId
     */
    private void saveCourseIndex(MqMessage mqMessage, Long courseId) {
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        // 取出第二个极端状态
        int stageTwo = mqMessageService.getStageTwo(taskId);

        //任务幂等性处理
        if(stageTwo>0){
            log.debug("课程索引信息已写入，无需执行...");
            return;
        }

        // 查询课程信息，调用搜素服务添加索引接口
        CoursePublish coursePublish = coursePublishMapper.selectById(courseId);

    }
}
