package com.step.model;

import com.step.tool.utils.JsonUtil;

/**
 * @author : Sun
 * @date : 2024/6/8  9:14
 */
public class HttpResponse<T> {

    private boolean success;
    private int statusCode;
    private String message;
    private T response;


    public static <T> HttpResponse<T> success(int statusCode, String responseString, Class<T> clazz) {
        HttpResponse<T> response = new HttpResponse<>();
        response.success = true;
        response.statusCode = statusCode;
        response.message = responseString;
        try {
            response.response = JsonUtil.parse(responseString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static HttpResponse error(String message) {
        HttpResponse response = new HttpResponse<>();
        response.success = false;
        response.statusCode = 400;
        response.message = message;
        return response;
    }

    public static HttpResponse empty(int statusCode) {
        HttpResponse response = new HttpResponse<>();
        response.success = true;
        response.statusCode = statusCode;
        response.message = "";
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}
