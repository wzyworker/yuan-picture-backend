package com.wzy.yuanpicturebackend.exception;

import lombok.Getter;

/**
 * @author wzy
 * @date 2025年03月23日 19:16
 * 自定义异常
 */

@Getter
public class BusinessException extends RuntimeException{
    private final int code;

    // 指定自定义错误码和自定义消息
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    // 指定公共错误码和消息
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    // 指定公共错误码和自定义消息
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
