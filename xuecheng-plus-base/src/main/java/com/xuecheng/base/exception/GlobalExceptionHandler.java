package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.xuecheng.base.exception
 * Description:
 *
 * @Author: Ronan
 * @Create 2024/2/17 - 16:13
 * @Version: v1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理
     * @param e 自定义异常
     * @return
     */
    @ExceptionHandler(LearnOnlineException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(LearnOnlineException e) {
        log.error("系统异常：{}", e.getErrMessage(), e);
        // SpringSecurity权限异常：AccessDeniedException，
        // 因为不想引入SpringSecurity依赖(会直接管控所有资源，进行拦截)，所以在这里进行判断
        if (e.getMessage().equals("不允许访问")) {
            return new RestErrorResponse("你没有权限操作");
        }
        return new RestErrorResponse(e.getErrMessage());
    }

    /**
     * 统一异常处理
     * @param e 异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e){
        //记录异常
        log.error("系统异常{}",e.getMessage(),e);
        //解析出异常信息
        return new RestErrorResponse(CommonError.UNKNOWN_ERROR.getErrMessage());
    }

    /**
     * @Validated 校验错误异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public RestErrorResponse handler(MethodArgumentNotValidException e) {
        log.error("运行时异常:-------------->",e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        // 存储错误信息
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(item -> errors.add(item.getDefaultMessage()));
        String errorMessage = StringUtils.join(errors, ",");
        return new RestErrorResponse(errorMessage);
    }
}
