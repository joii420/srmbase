package com.step.api.runtime.exception.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseException extends RuntimeException {

    public static final Logger log = LoggerFactory.getLogger(BaseException.class);
    private int statusCode = 500;
    private String code = "400";
    private String message;
    private boolean needRecord = false;

    public BaseException(Throwable e) {
        super(e);
        this.needRecord = true;
        this.message = e.getMessage();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }


    public BaseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public boolean isNeedRecord() {
        return needRecord;
    }

    public BaseException record() {
        this.needRecord = true;
        return this;
    }
}
