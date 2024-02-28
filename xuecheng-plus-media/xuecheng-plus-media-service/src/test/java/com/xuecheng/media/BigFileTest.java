package com.xuecheng.media;

import com.baomidou.mybatisplus.extension.conditions.update.ChainUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.*;

/**
 * ClassName: BigFileTest
 * Package: com.xuecheng.media
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/28 - 16:52
 * @Version: v1.0
 */
public class BigFileTest {

    @Test
    public void testChunk() throws Exception {
        // 源文件
        File sourceFile = new File("C:\\Users\\Ronan\\Pictures\\动漫\\Reincarnation.png");
        // 分块文件路径
        String chunkFilePath = "D:\\Code\\test\\chunk\\";
        // 分块文件大小
        int chunkSize = 1024 * 1024;   // 1MB
        // 分块数量
        int chunkNum = (int) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        // 使用流从源文件读数据，向分块文件中写入数据
        RandomAccessFile raf_r = new RandomAccessFile(sourceFile, "r");
        byte[] buffer = new byte[1024 * 4];
        for (int i = 0; i < chunkNum; i++) {
            File chunkFile = new File(chunkFilePath + i);
            RandomAccessFile raf_rw = new RandomAccessFile(chunkFile, "rw");
            int len = -1;
            while ((len = raf_r.read(buffer)) != -1) {
                raf_rw.write(buffer, 0, len);
                if (raf_rw.length() > chunkSize) {
                    break;
                }
            }
            raf_rw.close();
        }
        raf_r.close();
    }

    @Test
    public void testMerge() throws Exception {
        // 块文件路径
        File chuckFolder = new File("D:\\Code\\test\\chunk\\");
        // 源文件
        File sourceFile = new File("C:\\Users\\Ronan\\Pictures\\动漫\\Reincarnation.png");
        // 合并后文件
        File mergeFile = new File("D:\\Code\\test\\result\\result1.png");

        // 取出所有分块文件
        File[] files = chuckFolder.listFiles();
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, (o1, o2) -> {
            return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());  // 大于0交换，小于0不交换
        });

        // 向合并文件写入的流
        RandomAccessFile raf_rw = new RandomAccessFile(mergeFile, "rw");

        byte[] buffer = new byte[1024 * 4];
        int len = -1;
        // 遍历文件分块，向合并的文件写入
        for (File file : fileList) {
            // 向合并文件写入流
            RandomAccessFile raf_r = new RandomAccessFile(file, "r");
            while ((len = raf_r.read(buffer)) != -1) {
                raf_rw.write(buffer, 0, len);
            }
            raf_r.close();
        }
        raf_rw.close();

        // 合并后文件校验
        FileInputStream fis_merge = new FileInputStream(mergeFile);
        FileInputStream fis_source = new FileInputStream(sourceFile);
        if (DigestUtils.md5DigestAsHex(fis_merge).equals(DigestUtils.md5DigestAsHex(fis_source))) {
            System.out.println("文件合并成功");
        } else {
            System.out.println("文件合并失败");
        }
    }
}
