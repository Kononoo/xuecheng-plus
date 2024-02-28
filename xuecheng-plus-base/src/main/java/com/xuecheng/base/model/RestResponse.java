package com.xuecheng.base.model;

import lombok.Data;

/**
 * ClassName: RestResponse
 * Package: com.xuecheng.base.model
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/28 - 20:50
 * @Version: v1.0
 */
@Data
public class RestResponse<T> {
    /**
     * 响应编码,0为正常,-1错误
     */
    private int code;

    /**
     * 响应提示信息
     */
    private String msg;

    /**
     * 响应内容
     */
    private T result;

    {
        code = 1;
        msg = "success";
    }

    public RestResponse() { }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误信息的封装
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> RestResponse<T> validFail(String msg) {
        RestResponse<T> restResponse = new RestResponse<>();
        restResponse.setCode(-1);
        restResponse.setMsg(msg);
        return restResponse;
    }

    /**
     * 错误信息封装
     * @param result
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> RestResponse<T> validFail(T result, String msg) {
        RestResponse<T> restResponse = new RestResponse<>();
        restResponse.setCode(-1);
        restResponse.setMsg(msg);
        restResponse.setResult(result);
        return restResponse;
    }

    /**
     * 成功响应数据
     * @param result
     * @return
     * @param <T>
     */
    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<T>();
        response.setResult(result);
        return response;
    }

    /**
     * 成功响应数据
     * @param result
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> RestResponse<T> success(T result, String msg) {
        RestResponse<T> response = new RestResponse<T>();
        response.setMsg(msg);
        response.setResult(result);
        return response;
    }

    /**
     * 正常响应数据
     * @return
     * @param <T>
     */
    public static <T> RestResponse<T> success() {
        return new RestResponse<T>();
    }

    public Boolean isSuccessful() {
        return this.code == 0;
    }
}
