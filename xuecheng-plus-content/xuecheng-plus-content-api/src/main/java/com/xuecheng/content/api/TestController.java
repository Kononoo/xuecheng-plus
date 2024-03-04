package com.xuecheng.content.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * ClassName: TestController
 * Package: com.xuecheng.content.api
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/3/3 - 21:28
 * @Version: v1.0
 */
@Slf4j
@RestController
public class TestController {

    @GetMapping("/testFreemarker")
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test");   // 视图名，加上ftl找到模板
        modelAndView.addObject("name", "京子");   // 模型数据
        return modelAndView;
    }
}
