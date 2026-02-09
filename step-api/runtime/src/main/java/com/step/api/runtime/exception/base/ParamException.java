package com.step.api.runtime.exception.base;

/**
 * @author : Sun
 * @date : 2024/8/23  16:30
 */
public class ParamException extends RuntimeException {

    public ParamException(Throwable cause) {
        super(cause);
    }

    public ParamException(String message) {
        super(message);
    }

}
