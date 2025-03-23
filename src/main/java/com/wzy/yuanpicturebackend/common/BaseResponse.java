package com.wzy.yuanpicturebackend.common;

import com.wzy.yuanpicturebackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wzy
 * @date 2025年03月23日 19:33
 * 全局响应封装类
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;

    private String message;

    private T data;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, T data) {
        this(code, "", data);
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage(), null);
    }
}
