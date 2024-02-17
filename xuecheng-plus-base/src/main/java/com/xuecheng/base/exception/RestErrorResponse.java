package com.xuecheng.base.exception;

import java.io.Serializable;

/**
 * ClassName: RestErrorResponse
 * Package: com.xuecheng.base.exception
 * Description:
 *  和前端约定返回的异常信息模型
 * @Author: Ronan
 * @Create 2024/2/17 - 16:07
 * @Version: v1.0
 */
public class RestErrorResponse implements Serializable {
    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }
    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
