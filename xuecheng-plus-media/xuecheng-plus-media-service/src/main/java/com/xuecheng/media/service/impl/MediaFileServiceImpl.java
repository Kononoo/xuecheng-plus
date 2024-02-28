package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.LearnOnlineException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import javafx.beans.binding.MapExpression;
import javafx.beans.binding.ObjectExpression;
import kotlin.time.MeasureTimeKt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/9/10 8:58
 */
@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {
    @Resource
    private MediaFilesMapper mediaFilesMapper;
    @Resource
    private MinioClient minioClient;
    @Resource
    private MediaFileService currentProxy;

    @Value("${minio.bucket.files}")
    private String bucket_mediaFile;
    @Value("${minio.bucket.videofiles}")
    private String bucket_video;

    @Override
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();

        //分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;

    }

    // 事务管理成功条件：1 代理对象 2 Transactional注解  即：执行代理对象中有@Transactional注解的方法
    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String tempFilePath) {
        // 文件名
        String filename = uploadFileParamsDto.getFilename();
        // 获取minioType
        String extension = filename.substring(filename.lastIndexOf("."));
        String mimeType = getMimeType(extension);
        // 获取子目录
        String defaultFolderPath = getDefaultFolderPath();
        String fileMd5 = getFileMd5(new File(tempFilePath));
        String objectName = defaultFolderPath + fileMd5 + extension;

        // 上传文件到minio
        boolean result = putMediaFileToMinio(tempFilePath, mimeType, bucket_mediaFile, objectName);
        if (!result) {
            throw new LearnOnlineException("上传文件失败");
        }
        // 写入数据库
        // 如果直接使用原始对象方法，则事务会失效(没有代理对象继续管理)
        MediaFiles mediaFiles = currentProxy.addMediaFileToDb(companyId, uploadFileParamsDto, bucket_mediaFile, fileMd5, objectName);
        if (mediaFiles == null) {
            throw new LearnOnlineException("文件上传后保存信息失败");
        }
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);
        return uploadFileResultDto;
    }

    /**
     * 文件信息插入数据库
     *
     * @param companyId
     * @param uploadFileParamsDto
     * @param bucket
     * @param fileMd5
     * @param objectName
     * @return
     */
    @Transactional
    public MediaFiles addMediaFileToDb(Long companyId, UploadFileParamsDto uploadFileParamsDto, String bucket, String fileMd5, String objectName) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles == null) {
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(uploadFileParamsDto, mediaFiles);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setBucket(bucket);
            mediaFiles.setFilePath(objectName);
            mediaFiles.setUrl("/" + bucket + "/" + objectName);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            mediaFiles.setAuditStatus("002003");
            // 插入数据库
            int insert = mediaFilesMapper.insert(mediaFiles);
            if (insert == 0) {
                log.error("文件信息保存失败");
                return null;
            }
        }
        return mediaFiles;
    }


    private String getFileMd5(File file) {
        try {
            return DigestUtils.md5DigestAsHex(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取文件默认存储路径
    private String getDefaultFolderPath() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(new Date()) + "/";
    }

    // 根据扩展名获取mimeType
    private String getMimeType(String extension) {
        if (extension == null) {
            extension = "";
        }
        // 根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch == null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    /**
     * 将文件上传到 minio
     *
     * @param tempFilePath 要上传的文件路径
     * @param mimeType     媒体类型
     * @param bucket       桶
     * @param objectName   对象名
     * @return
     */
    private boolean putMediaFileToMinio(String tempFilePath, String mimeType, String bucket, String objectName) {
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .filename(tempFilePath)
                    .contentType(mimeType)
                    .object(objectName)
                    .build();
            // 上传文件
            minioClient.uploadObject(uploadObjectArgs);
            log.info("上传文件成功");
            return true;
        } catch (Exception e) {
            log.error("上传文件出错：{}", e.getMessage());
        }
        return false;
    }

    @Override
    public RestResponse<Boolean> checkFile(String fileMd5) {
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if (mediaFiles != null) {
            // 获取桶和文件路径
            String bucket = mediaFiles.getBucket();
            String filePath = mediaFiles.getFilePath();

            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filePath)
                    .build();
            try {
                GetObjectResponse object = minioClient.getObject(getObjectArgs);
                if (object != null) {
                    // 文件已经存在
                    return RestResponse.success(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RestResponse.success(false);
    }

    // 得到分块文件目录
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.charAt(0) + "/" + fileMd5.charAt(1) + "/" + "chunk" + "/";
    }

    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {
        // 根据Md5得到分块文件目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 如果关联数据库再查询 minio
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucket_video)
                .object(chunkFileFolderPath + chunkIndex)
                .build();
        try {
            GetObjectResponse object = minioClient.getObject(getObjectArgs);
            if (object != null) {
                return RestResponse.success(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 文件不存在
        return RestResponse.success(false);
    }

    @Override
    public RestResponse uploadChunk(String fileMd5, int chunk, String tempFilePath) {
        // 分块文件路径
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;
        // 获取mimeType
        String mimeType = getMimeType(null);
        // 将分块文件上传到minio
        boolean b = putMediaFileToMinio(tempFilePath, mimeType, bucket_video, chunkFilePath);
        if (!b) {
            return RestResponse.success(false, "上传分块文件失败");
        }
        // 上传成功
        return RestResponse.success(true);
    }

    @Override
    public RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto) {
        // 分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 找到所有文件的分块
        List<ComposeSource> composeSources = Stream.iterate(0, i -> ++i).limit(chunkTotal).map(i -> ComposeSource.builder()
                .bucket(bucket_video)
                .object(chunkFileFolderPath + i)
                .build()).collect(Collectors.toList());
        // 获取文件扩展名
        String filename = uploadFileParamsDto.getFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        // 合并后的文件名
        String objectName = fileMd5 + extension;
        // 指定合并文件信息
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket(bucket_video)
                .object(objectName)
                .sources(composeSources)
                .build();
        try {
            minioClient.composeObject(composeObjectArgs)
        } catch (Exception e) {
            log.error("合并文件出错,bucket:{},objectName:{},错误信息:{}", bucket_video, objectName, e.getMessage());
            return RestResponse.validFail(false, "合并文件出现异常");
        }
        //===========校验合并后的和源文件是否一致，视频上传才成功===========
        File file = downloadFileFromMinio(bucket_video, objectName);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            String mergeFile_md5 = DigestUtils.md5DigestAsHex(inputStream);
            if (!fileMd5.equals(mergeFile_md5)) {
                log.error("校验合并文件md5值不一致,原始文件:{},合并文件:{}", fileMd5, mergeFile_md5);
                return RestResponse.validFail(false, "文件校验失败");
            }
            // 文件大小
            uploadFileParamsDto.setFileSize(file.length());
        } catch (Exception e) {
            return RestResponse.validFail(false, "文件校验失败");
        }
        //==============将文件信息入库============
        MediaFiles mediaFiles = currentProxy.addMediaFileToDb(companyId, uploadFileParamsDto, bucket_video, fileMd5, objectName);
        if (mediaFiles == null) {
            return RestResponse.validFail(false, "文件入库失败");
        }
        //==========清理分块文件=========
        clearChunkFiles()

    }

    public File downloadFileFromMinio(String bucket, String objectName) {
        // 临时文件
        File minioFile = null;
        FileOutputStream outputStream = null;
        try {
            GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            // 创建临时文件
            minioFile = File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(object, outputStream);
            return minioFile;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 清除分块文件
     * @param chunkFileFolderPath 分块文件路径
     * @param chunkTotal 分块文件总数
     */
    private void clearChunkFiles(String chunkFileFolderPath, int chunkTotal) {
        List<DeleteObject> collect = Stream.iterate(0, i -> ++i).limit(chunkTotal).map(i ->
                new DeleteObject(chunkFileFolderPath + i)).collect(Collectors.toList());
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(bucket_video)
                .objects(collect)
                .build();
        // 删除文件
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
        // 真正删除文件
        results.forEach(f -> {
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
