package com.snomyc.common.base.security.exception;

/**
 * 
 * 类名称：TokenException<br>
 * 类描述：令牌异常<br>
 * @version v1.0
 *
 */
public class TokenException extends RuntimeException {

    private static final long serialVersionUID = 8391869486329200571L;

    /**
     * 
     * 创建一个新的实例 TokenException.     
     * @param message
     */
    public TokenException(String message) {
        super(message);
    }
}
