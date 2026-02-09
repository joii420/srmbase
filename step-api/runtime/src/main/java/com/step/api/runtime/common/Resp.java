package com.step.api.runtime.common;

import lombok.Data;

import java.io.Serializable;

public class Resp<T> implements Serializable {

    private String code;
    private boolean success;
    private String message;
    private T data;


    private Resp() {
        this.message = "";
    }

    public static <T> Resp<T> success() {
        Resp<T> response = new Resp<>();
        response.code = "200";
        response.success = true;
        return response;
    }

    public static <T> Resp<T> success(String code) {
        Resp<T> response = new Resp<>();
        response.code = code;
        response.success = true;
        return response;
    }

    public static <T> Resp<T> data(String code, T data) {
        Resp<T> response = new Resp<>();
        response.code = code;
        response.success = true;
        response.data = data;
        return response;
    }

    public static <T> Resp<T> data(T data) {
        Resp<T> response = new Resp<>();
        response.code = "200";
        response.success = true;
        response.data = data;
        return response;
    }

    public static <T> Resp<T> failed(String code, String message) {
        Resp<T> response = new Resp<>();
        response.code = code;
        response.success = false;
        response.message = message;
        return response;
    }

    public static <T> Resp<T> failed(String message) {
        Resp<T> response = new Resp<>();
        response.code = "500";
        response.success = false;
        response.message = message;
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
