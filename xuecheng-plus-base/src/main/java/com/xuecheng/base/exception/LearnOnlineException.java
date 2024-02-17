package com.xuecheng.base.exception;

/**
 * ClassName: LearnOnlineException
 * Package: com.xuecheng.base.exception
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/17 - 16:10
 * @Version: v1.0
 */
public class LearnOnlineException extends RuntimeException {
    private String errMessage;

    public LearnOnlineException() {
    }

    public LearnOnlineException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public static void cast(String message) {
        throw new LearnOnlineException(message);
    }

    public static void cast(CommonError error) {
        throw new LearnOnlineException(error.getErrMessage());
    }

}
