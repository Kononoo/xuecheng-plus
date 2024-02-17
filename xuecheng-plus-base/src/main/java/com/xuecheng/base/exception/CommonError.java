package com.xuecheng.base.exception;

/**
 * ClassName: CommonError
 * Package: com.xuecheng.base.exception
 * Description:
 *  通用错误信息
 * @Author: Ronan
 * @Create 2024/2/17 - 16:04
 * @Version: v1.0
 */
public enum CommonError {
    UNKNOWN_ERROR("执行过程异常，请重试"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("对象为空"),
    QUERY_NULL("查询结果为空"),
    REQUEST_NULL("请求参数为空");

    private final String errMessage;

    CommonError(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
