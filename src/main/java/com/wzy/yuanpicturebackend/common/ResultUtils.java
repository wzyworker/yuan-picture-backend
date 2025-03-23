package com.wzy.yuanpicturebackend.common;

import com.wzy.yuanpicturebackend.exception.ErrorCode;

/**
 * @author wzy
 * @date 2025年03月23日 19:42
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, "ok", data);
    }

    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse<?> error(int code, String message){
        return new BaseResponse<>(code, message, null);
    }

    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), message, null);
    }
}
