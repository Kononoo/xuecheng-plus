package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.vo.TeachplanVo;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: TeachplanController
 * Package: com.xuecheng.content.api
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/17 - 19:28
 * @Version: v1.0
 */
@Slf4j
@Api(value = "课程计划接口", tags = "课程计划管理接口")
@RequestMapping("/teachplan")
@RestController
public class TeachplanController {
    @Resource
    private TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanVo> getTreeNodes(@PathVariable @ApiParam("课程Id") Long courseId) {
        List<TeachplanVo> teachplanTree = teachplanService.getTeachPlanTree(courseId);
        return teachplanTree;
    }

    @ApiOperation("创建或修改课程计划")
    @PostMapping
    public void associationMedia(@RequestBody SaveTeachplanDto teachplanDto) {
        teachplanService.saveTeachplan(teachplanDto);
    }
}
