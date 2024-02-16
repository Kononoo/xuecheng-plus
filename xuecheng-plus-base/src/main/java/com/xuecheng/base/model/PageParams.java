package com.xuecheng.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: PageParams
 * Package: com.xuecheng.base.model
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/16 - 15:36
 * @Version: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    // 当前页码
    @ApiModelProperty("页码")
    private Long pageNo = 1L;

    @ApiModelProperty("每页记录数")
    private Long pageSize = 30L;
}
