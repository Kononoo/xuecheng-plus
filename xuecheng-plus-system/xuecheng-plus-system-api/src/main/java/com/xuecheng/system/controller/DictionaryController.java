package com.xuecheng.system.controller;

import com.xuecheng.system.model.po.Dictionary;
import com.xuecheng.system.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: DictionaryController
 * Package: com.xuecheng.system.controller
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 22:19
 * @Version: v1.0
 */
@Slf4j
@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @ApiOperation("查询所有字典数据")
    @GetMapping("/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @GetMapping("/code/{code}")
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }

}
