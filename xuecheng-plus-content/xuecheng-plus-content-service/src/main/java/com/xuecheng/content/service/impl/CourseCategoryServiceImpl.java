package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author ronan
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {
    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        // 查出所有分类信息
        List<CourseCategoryTreeDto> courseCategoryTreeDtoList = courseCategoryMapper.selectTreeNodes(id);
        // 定义最终返回的List，封装成List<CourseCategoryTreeDto>
        List<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>(1);
        // 将list转成map，key就是结点的id，value就是CourseCategoryTreeDto对象，同时有key冲突以新的为主
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtoList.stream()
                .filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toMap(CourseCategory::getId, value -> value, (key1, key2) -> key2));

        // 从头遍历 List<CourseCategoryTreeDto> ，一边遍历一边找子节点放在父节点的childrenTreeNodes
        courseCategoryTreeDtoList.stream().filter(item -> !id.equals(item.getId())).forEach(item -> {
            if (item.getParentid().equals(id)) {
                courseCategoryList.add(item);
            }
            // 找到节点的父节点，添加到父节点的儿子中
            CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
            if (courseCategoryTreeDto != null) {
                if (courseCategoryTreeDto.getChildrenTreeNodes() == null) {
                    List<CourseCategoryTreeDto> childrenList = new ArrayList<>();
                    courseCategoryTreeDto.setChildrenTreeNodes(childrenList);
                }
                courseCategoryTreeDto.getChildrenTreeNodes().add(item);
            }
        });
        return courseCategoryList;
    }
}
