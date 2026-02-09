package com.step.api.runtime.exception.base;

public class SimpleErrorResponse {

    private Integer statusCode;
    private String code;
    private long expId = 0;
    private boolean success;
    private String message;


    public SimpleErrorResponse(Integer statusCode, String code, String message, String lang) {
        this.statusCode = statusCode;
        this.code = code;
        this.success = false;
        this.message = message;
    }

    public SimpleErrorResponse(Integer statusCode, String code, String message) {
        this.statusCode = statusCode;
        this.code = code;
        this.success = false;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getExpId() {
        return expId;
    }

    public void setExpId(long expId) {
        this.expId = expId;
    }
}
