package com.xuecheng.media;

import com.alibaba.nacos.common.utils.IoUtils;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * ClassName: MinioTest
 * Package: com.xuecheng.media
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/26 - 23:01
 * @Version: v1.0
 */
public class MinioTest {

    private MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.56.100:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void test_upload() throws Exception {
        // 通过扩展名得到媒体资源类型 mineType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".png");
        String minType = MediaType.APPLICATION_OCTET_STREAM_VALUE;  // 未知类型，通用mineType字节流
        if (extensionMatch != null) {
            minType = extensionMatch.getMimeType();
        }

        // 上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")   // 桶
                .filename("C:\\Users\\Ronan\\Pictures\\动漫\\hhh.png") // 指定本地文件
//                .object("hhh.png")   // 对象名
                .object("test/hhh.png")   // 存放在子目录
                .contentType(minType)
                .build();

        // 上传文件

        minioClient.uploadObject(uploadObjectArgs);
    }

    @Test
    public void test_delete() throws Exception {
        minioClient.listBuckets().forEach(System.out::println);
        // 删除文件
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                .object("hhh.png")
                .build();

        minioClient.removeObject(removeObjectArgs);
    }

    @Test
    public void test_getFile() throws Exception{
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket("testbucket")
                .object("test/hhh.png")
                .build();

        GetObjectResponse getObjectResponse = minioClient.getObject(getObjectArgs);
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\Code\\hhh.png"));
        byte[] buffer = new byte[1024];
        int byteRead;
        while ((byteRead = getObjectResponse.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteRead);
        }
        outputStream.close();
        System.out.println("进行校验...");
//        IoUtils.copy(getObjectResponse, outputStream);
        // 校验文件的完整性，对文件内容进行md5加密
        String source_md5 = DigestUtils.md5DigestAsHex(getObjectResponse);
        String local_md5 = DigestUtils.md5DigestAsHex(new FileInputStream(new File("D:\\Code\\hhh.png")));
        if (source_md5.equals(local_md5)) {
            System.out.println("下载成功");
        } else {
            System.out.println("校验有误，下载失败！");
        }
    }
}
