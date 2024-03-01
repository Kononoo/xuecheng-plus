package com.xuecheng.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {


    List<MediaProcess> selectListBySharedIndex(@Param("shardIndex") int shardIndex, @Param("shardTotal") int shardTotal, @Param("count") int count);


    /**
     * 开启一个任务
     * @param id 任务Id
     * @return 更新记录数
     */
    int startTask(long id);
}
