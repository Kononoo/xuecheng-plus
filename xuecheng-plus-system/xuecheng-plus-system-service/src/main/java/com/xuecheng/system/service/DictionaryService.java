package com.xuecheng.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.system.model.po.Dictionary;

import java.util.List;

/**
 * ClassName: DictionaryService
 * Package: com.xuecheng.system.service
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 22:07
 * @Version: v1.0
 */
public interface DictionaryService extends IService<Dictionary> {
    /**
     * 查询所有数据字典内容
     * @return
     */
    List<Dictionary> queryAll();

    /**
     * 根据code查询数据字典
     * @param code -- String 数据字典Code
     * @return
     */
    Dictionary getByCode(String code);
}
