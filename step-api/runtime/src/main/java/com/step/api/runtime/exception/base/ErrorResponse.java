package com.step.api.runtime.exception.base;

import java.util.Optional;

public class ErrorResponse {

    private Integer statusCode;
    private String code;
    private boolean success;
    private String message;
    private String lang;
    private String sqlCode;
    private String sqlMessage;

    public ErrorResponse() {
        this.lang = "en_US";
    }

    public ErrorResponse(Integer statusCode, String code, String message, String lang) {
        this.statusCode = statusCode;
        this.code = code;
        this.success = false;
        this.message = message;
        this.lang = Optional.ofNullable(lang).orElse("en_US");
    }

    public ErrorResponse(Integer statusCode, String code, String message) {
        this.statusCode = statusCode;
        this.code = code;
        this.success = false;
        this.message = message;
        this.lang = "en_US";
    }

    public ErrorResponse(Integer statusCode, String code, String message, String sqlCode, String sqlMessage) {
        this.statusCode = statusCode;
        this.code = code;
        this.success = false;
        this.message = message;
        this.sqlCode = sqlCode;
        this.sqlMessage = sqlMessage;
        this.lang = "en_US";
    }

    public ErrorResponse(Integer statusCode, String code, String message, String lang, String sqlCode, String sqlMessage) {
        this.statusCode = statusCode;
        this.code = code;
        this.success = false;
        this.message = message;
        this.sqlCode = sqlCode;
        this.sqlMessage = sqlMessage;
        this.lang = "en_US";
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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

    public String getSqlCode() {
        return sqlCode;
    }

    public void setSqlCode(String sqlCode) {
        this.sqlCode = sqlCode;
    }

    public String getSqlMessage() {
        return sqlMessage;
    }

    public void setSqlMessage(String sqlMessage) {
        this.sqlMessage = sqlMessage;
    }
}
