package com.step.quartz.runtime.exception;

import com.step.api.runtime.exception.base.BaseException;

public class QuartzException extends BaseException {

    private static int statusCode = 200;

    public QuartzException(String message) {
        super(statusCode, message);
    }

}
