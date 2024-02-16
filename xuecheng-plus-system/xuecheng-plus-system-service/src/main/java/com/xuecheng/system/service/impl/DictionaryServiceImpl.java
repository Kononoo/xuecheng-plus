package com.xuecheng.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.system.mapper.DictionaryMapper;
import com.xuecheng.system.model.po.Dictionary;
import com.xuecheng.system.service.DictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: DictionaryServiceImpl
 * Package: com.xuecheng.system.service.impl
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 22:09
 * @Version: v1.0
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {
    @Resource
    private DictionaryMapper dictionaryMapper;

    @Override
    public List<Dictionary> queryAll() {
        return this.list();
    }

    @Override
    public Dictionary getByCode(String code) {
        Dictionary dictionary = lambdaQuery().eq(Dictionary::getCode, code).one();
        return dictionary;
    }
}
