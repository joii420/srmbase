package com.step.mq.runtime.mqtt.exception;

import com.step.api.runtime.exception.base.BaseException;

/**
 * @author : Sun
 * @date : 2023/8/2  15:21
 */
public class MQException extends BaseException {

    private static int statusCode = 500;

    public MQException(String message) {
        super(statusCode, message);
    }


}
