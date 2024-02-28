package com.xuecheng.media.api;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xuecheng.MediaApplication;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.service.MediaFileService;
import groovy.lang.DelegatesTo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * ClassName: BigFilesController
 * Package: com.xuecheng.media.api
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/28 - 20:48
 * @Version: v1.0
 */
@Slf4j
@Api(value = "大文件上传接口", tags = "大文件上传接口")
@RestController
public class BigFilesController {
    @Resource
    private MediaFileService mediaFileService;

    @ApiOperation(value = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkFile(@RequestParam("fileMd5") String fileMd5) {
        RestResponse<Boolean> booleanRestResponse = mediaFileService.checkFile(fileMd5);
        return booleanRestResponse;
    }

    @ApiOperation(value = "分块文件上传前的检测")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkChunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) {
        RestResponse<Boolean> booleanRestResponse = mediaFileService.checkChunk(fileMd5, chunk);
        return booleanRestResponse;
    }

    @ApiOperation(value = "上传文件分块")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadFileChunk(@RequestParam("file") MultipartFile file,
                                        @RequestParam("fileMd5") String fileMd5,
                                        @RequestParam("chunk") int chunk) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("minio", "temp");
        file.transferTo(tempFile);
        // 获取文件路径
        String tempFilePath = tempFile.getAbsolutePath();

        RestResponse restResponse = mediaFileService.uploadChunk(fileMd5, chunk, tempFilePath);
        return restResponse;
    }

    @ApiOperation(value = "合并分块")
    @PostMapping("/upload/mergechunks")
    public RestResponse mergeChunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") Integer chunkTotal) {
        Long companyId = 1232141425L;
        // 文件信息对象
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFilename(fileName);
        uploadFileParamsDto.setTags("视频文件");
        uploadFileParamsDto.setFileType("001002");
        RestResponse restResponse = mediaFileService.mergeChunks(companyId, fileMd5, chunkTotal, uploadFileParamsDto);
        return restResponse;
    }
}
