package com.xuecheng.content.api;

import com.xuecheng.content.model.vo.CoursePreviewVo;
import com.xuecheng.content.service.CoursePublishService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * ClassName: CoursePublishController
 * Package: com.xuecheng.content.api
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/3/4 - 14:56
 * @Version: v1.0
 */
@Controller
public class CoursePublishController {

    @Resource
    private CoursePublishService coursePublishService;

    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId) {
        ModelAndView modelAndView = new ModelAndView();
        // 查询课程信息作为数据
        CoursePreviewVo coursePreviewVo = coursePublishService.getCoursePreviewInfo(courseId);
        modelAndView.addObject("model", coursePreviewVo);
        modelAndView.setViewName("course_template");  // 指定模板
        return modelAndView;
    }


}
