package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.vo.TeachplanVo;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
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
@RestController
public class TeachplanController {
    @Resource
    private TeachplanService teachplanService;

    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanVo> getTreeNodes(@PathVariable @ApiParam("课程Id") Long courseId) {
        List<TeachplanVo> teachplanTree = teachplanService.getTeachPlanTree(courseId);
        return teachplanTree;
    }

    @ApiOperation("创建或修改课程计划")
    @PostMapping("/teachplan")
    public void associationMedia(@RequestBody SaveTeachplanDto teachplanDto) {
        teachplanService.saveTeachplan(teachplanDto);
    }

    @ApiOperation("课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody @Validated BindTeachplanMediaDto bindTeachplanMediaDto) {
        teachplanService.associationMedia(bindTeachplanMediaDto);
    }

    @ApiOperation("删除课程计划")
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachPlan(@PathVariable Long teachplanId) {
        Assert.notNull(teachplanId, "课程计划ID不能为空");
        teachplanService.deleteTeachPlan(teachplanId);
    }

    @ApiOperation("课程计划排序")
    @PostMapping("/teachplan/{moveType}/{teachplanId}")
    public void moveTeachplanOrder(@PathVariable String moveType, @PathVariable Long teachplanId) {
        Assert.notNull(teachplanId, "课程计划ID不能为空");
        teachplanService.orderByTeachplan(moveType, teachplanId);
    }
}
