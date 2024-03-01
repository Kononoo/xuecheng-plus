package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;

import java.io.File;

/**
 * @author Mr.M
 * @version 1.0
 * @description 媒资文件管理业务类
 * @date 2022/9/10 8:55
 */
public interface MediaFileService {

    /**
     * @param pageParams          分页参数
     * @param queryMediaParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
     * @description 媒资文件查询方法
     * @author Mr.M
     * @date 2022/9/10 8:57
     */
    PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

    /**
     * 将文件上传minio
     *
     * @param companyId           机构id
     * @param uploadFileParamsDto 文件信息
     * @param tempFilePath        临时文件路径
     * @return UploadFileResultDto
     */
    UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String tempFilePath);

    /**
     * 将文件上传到 minio
     *
     * @param tempFilePath 要上传的文件路径
     * @param mimeType     媒体类型
     * @param bucket       桶
     * @param objectName   对象名
     * @return
     */
    boolean putMediaFileToMinio(String tempFilePath, String mimeType, String bucket, String objectName);

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
    MediaFiles addMediaFileToDb(Long companyId, UploadFileParamsDto uploadFileParamsDto, String bucket, String fileMd5, String objectName);

    /**
     * 检查文件是否存在
     *
     * @param fileMd5
     * @return
     */
    RestResponse<Boolean> checkFile(String fileMd5);

    /**
     * 检测分块是否存在
     *
     * @param fileMd5
     * @param chunk
     * @return
     */
    RestResponse<Boolean> checkChunk(String fileMd5, int chunk);

    /**
     * 上传文件分块
     *
     * @param fileMd5      文件md5
     * @param chunk        分块序号
     * @param tempFilePath 分块文件本地路径
     * @return
     */
    RestResponse uploadChunk(String fileMd5, int chunk, String tempFilePath);

    /**
     * 合并文件分块
     *
     * @param companyId
     * @param fileMd5
     * @param chunkTotal
     * @param uploadFileParamsDto
     * @return
     */
    RestResponse mergeChunks(Long companyId, String fileMd5, int chunkTotal, UploadFileParamsDto uploadFileParamsDto);

    /**
     * 下载minio文件到本地
     *
     * @param bucket     桶
     * @param objectName 文件名
     * @return 文件
     */
    File downloadFileFromMinio(String bucket, String objectName)
}
