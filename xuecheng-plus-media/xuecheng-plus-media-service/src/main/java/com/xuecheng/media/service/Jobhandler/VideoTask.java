package com.xuecheng.media.service.Jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileService;
import com.xuecheng.media.service.MediaProcessService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * ClassName: VideoTask
 * Package: com.xuecheng.media.service.Jobhandler
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/3/1 - 18:59
 * @Version: v1.0
 */
@Slf4j
@Component
public class VideoTask {

    @Resource
    private MediaProcessService mediaProcessService;
    @Resource
    private MediaFileService mediaFileService;

    @Value("${videoProcess.ffmpegPath}")
    private String ffmpegPath;

    @XxlJob("videoJobHandler")
    public void videoJobHandler() throws InterruptedException {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();  // 执行器序号，0开始
        int shardTotal = XxlJobHelper.getShardTotal();  // 执行器总数

        // 查询待处理任务
        int processors = Runtime.getRuntime().availableProcessors();
        List<MediaProcess> mediaProcessList = mediaProcessService.getMediaProcessList(shardIndex, shardTotal, processors);
        // 获取任务数量，创建线程池
        int size = mediaProcessList.size();
        if (size == 0) {
            return;
        }
        // 创建线程池，计数器
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        mediaProcessList.forEach(mediaProcess -> {
            executorService.execute(() -> {
                try {
                    // 1 开启任务
                    Long taskId = mediaProcess.getId();
                    String fileId = mediaProcess.getFileId();
                    boolean b = mediaProcessService.startTask(taskId);
                    if (!b) {
                        log.debug("抢占任务失败");
                    }

                    // 下载minio视频到本地
                    String bucket = mediaProcess.getBucket();
                    String filePath = mediaProcess.getFilePath();
                    File file = mediaFileService.downloadFileFromMinio(bucket, filePath);
                    if (file == null) {
                        log.debug("下载视频出错,任务id:{},bucket:{},objectName:{}", taskId, bucket, filePath);
                        // 保存任务处理失败的结果
                        mediaProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "下载视频到本地失败");
                        return;
                    }

                    // 2 执行视频转码
                    // 源文件路径, 转换后的mp4文件名称
                    String video_path = file.getAbsolutePath();
                    String mp4_name = fileId + ".mp4";
                    File mp4File;
                    try {
                        mp4File = File.createTempFile("minio", ".mp4");
                    } catch (IOException e) {
                        log.debug("创建临时文件异常,{}", e.getMessage());
                        //保存任务处理失败的结果
                        mediaProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "创建临时文件异常");
                        return;
                    }
                    String mp4FilePath = mp4File.getAbsolutePath();
                    // 视频转码
                    Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegPath, video_path, mp4_name, mp4FilePath);
                    String result = videoUtil.generateMp4();
                    if (!"success".equals(result)) {
                        log.debug("视频转码失败,原因:{},bucket:{},objectName:{},", result, bucket, filePath);
                        mediaProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, result);
                        return;
                    }

                    // 3 上传到Minio
                    boolean isPutSuccess = mediaFileService.putMediaFileToMinio(mp4FilePath, "video/mp4", bucket, filePath);
                    if (!isPutSuccess) {
                        log.debug("上传mp4到minio失败,taskid:{}", taskId);
                        mediaProcessService.saveProcessFinishStatus(taskId, "3", fileId, null, "上传mp4到minio失败");
                        return;
                    }


                    // 4 保存任务处理结果
                    String url = getFilePath(fileId, ".mp4");
                    // 更新任务状态
                    mediaProcessService.saveProcessFinishStatus(taskId, "2", fileId, url, "创建临时文件成功");
                } finally {
                    //计算器减去1
                    countDownLatch.countDown();
                }
            });
        });

        //阻塞,指定最大限制的等待时间，阻塞最多等待一定的时间后就解除阻塞
        countDownLatch.await(30, TimeUnit.MINUTES);

    }

    private String getFilePath(String fileId, String extension) {
        return fileId.charAt(0) + "/" + fileId.charAt(1) + "/" + fileId + "/" + fileId + extension;

    }
}
