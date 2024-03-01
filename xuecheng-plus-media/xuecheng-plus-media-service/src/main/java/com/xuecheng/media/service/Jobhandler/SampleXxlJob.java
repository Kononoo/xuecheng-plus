package com.xuecheng.media.service.Jobhandler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: SampleXxlJob
 * Package: com.xuecheng.media.service.Jobhandler
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/29 - 15:58
 * @Version: v1.0
 */
@Component
public class SampleXxlJob {
    private static Logger logger = LoggerFactory.getLogger(SampleXxlJob.class);


    @XxlJob("demoJobHandler")
    public void demoJobHandler() {
        System.out.println("处理视频...");
    }

    @XxlJob("demoJobHandler2")
    public void demoJobHandler2() {
        System.out.println("处理图片...");
    }

    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();  // 执行器序号，0开始
        int shardTotal = XxlJobHelper.getShardTotal();  // 执行器总数

    }
}
