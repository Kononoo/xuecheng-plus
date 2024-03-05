package com.xuecheng.content;

import com.xuecheng.content.model.vo.CoursePreviewVo;
import com.xuecheng.content.service.CoursePublishPreService;
import com.xuecheng.content.service.CoursePublishService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @ClassName: FreemarkerTest
 * @Package: com.xuecheng.content
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/5 - 12:38
 * @Version: v1.0
 */
@Slf4j
@Component
public class FreemarkerTest {

    @Resource
    private CoursePublishService coursePublishService;

    @Test
    public void testGenerateHtmlByTemplate() throws Exception {
        Configuration configuration = new Configuration(Configuration.getVersion());

        // 拿到classpath资料
        String classpath = this.getClass().getResource("/").getPath();
        // 指定模板的目录
        configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates"));
        // 指定编码
        configuration.setDefaultEncoding("utf-8");
        // 那到模板
        Template template = configuration.getTemplate("course_template.ftl");

        // 准备数据
        CoursePreviewVo coursePreviewInfo = coursePublishService.getCoursePreviewInfo(120L);
        HashMap<String, CoursePreviewVo> map = new HashMap<>(1);
        map.put("model", coursePreviewInfo);

        // 输出指定文件
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream inputStream = IOUtils.toInputStream(html, "utf-8");
        FileOutputStream outputStream = new FileOutputStream("D:\\Code\\upload\\120.html");
        int len = -1;
        byte[] buffer = new byte[1024 * 4];
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
//        IOUtils.copy(inputStream, outputStream);
    }
}
