package com.snomyc.common.base.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.snomyc.common.base.domain.ResponseConstant;
import com.snomyc.common.base.domain.ResponseEntity;
import com.snomyc.common.base.security.exception.TokenException;

/**
 * 
 * 类名称：ExceptionAdvice<br>
 * 类描述：统一异常处理<br>
 * @version v1.0
 *
 */
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败", e);
        return new ResponseEntity().failure(ResponseConstant.CODE_000, "无法解析JSON");
    }
    
    /**
     * 401 - Unauthorized Request
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenException.class)
    public ResponseEntity handleTokenException(TokenException e) {
        logger.error("登录信息失效，请重新登录", e);
        return new ResponseEntity().failure(ResponseConstant.CODE_401, "登录信息失效，请重新登录");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法", e);
        return new ResponseEntity().failure(ResponseConstant.CODE_000, "不支持当前请求方法");
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("不支持当前媒体类型", e);
        return new ResponseEntity().failure(ResponseConstant.CODE_000, "不支持的内容类型");
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        logger.error("服务运行异常", e);
        return new ResponseEntity().failure(ResponseConstant.CODE_000, "服务运行异常:" + e.getMessage());
    }
}
