package com.xuecheng.media.api;

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.base.utils.StringUtil;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName: MediaOpenController
 * @Package: com.xuecheng.media.api
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/4 - 16:52
 * @Version: v1.0
 */
@Slf4j
@RestController
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
public class MediaOpenController {
    @Resource
    private MediaFileService mediaFileService;

    @ApiOperation("预览文件")
    @GetMapping("/preview/{mediaId}")
    public RestResponse<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        // 查询媒资文件
        MediaFiles mediaFiles = mediaFileService.getFileById(mediaId);

        if (mediaFiles == null) {
            return RestResponse.validFail("找不到视频");
        }
        // 获取视频播放地址
        String url = mediaFiles.getUrl();
        if (StringUtils.isEmpty(url)) {
            return RestResponse.validFail("视频正在处理中");
        }
        return RestResponse.success(mediaFiles.getUrl());
    }
}
