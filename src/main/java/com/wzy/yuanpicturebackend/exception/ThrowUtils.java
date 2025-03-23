package com.wzy.yuanpicturebackend.exception;

/**
 * @author wzy
 * @date 2025年03月23日
 * 抛出异常工具类
 */
public class ThrowUtils {
    /**
     * 条件成立抛异常
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立抛异常
     * @param condition 条件
     * @param errorCode 指定错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立抛异常
     * @param condition 条件
     * @param errorCode 指定错误码
     * @param message  自定义消息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
