package com.xuecheng.content.feignclient.fallback;

import com.xuecheng.content.feignclient.MediaServiceClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: MediaServiceClientFallbackFactory
 * @Package: com.xuecheng.content.feignclient.fallback
 * @Description:
 * @Author: Ronan
 * @Create 2024/3/5 - 14:30
 * @Version: v1.0
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
    // 拿到了熔断的异常信息throwable
    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient() {
            @Override
            public String upload(MultipartFile filedata, String objectName) {
                log.debug("远程调用上传文件的接口发生熔断：{}", throwable.toString(), throwable);
                return null;
            }
        };
    }
}
