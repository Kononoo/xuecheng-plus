package com.xuecheng.base.model;

import com.xuecheng.base.utils.PasswordUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: PageResult
 * Package: com.xuecheng.base.model
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 15:38
 * @Version: v1.0
 */
@Data
public class PageResult<T> implements Serializable {
    // 数据列表
    private List<T> items;

    // 总记录数
    private long counts;

    // 当前页码
    private long page;

    // 每页记录数
    private long pageSize;

    public PageResult(List<T> items, long counts, long page, long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * 构造分页返回参数
     * @param items
     * @param counts
     * @param page
     * @param pageSize
     * @return
     * @param <T>
     */
    public static <T> PageResult<T> of(List<T> items, long counts, long page, long pageSize) {
         return new PageResult<>(items, counts, page, pageSize);
    }
}
