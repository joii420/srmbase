/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.step.api.runtime.common;


import com.step.api.runtime.core.IResult;
import com.step.api.runtime.exception.ServerCode;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 *
 * @author Chill
 */

//@ApiModel(description = "返回信息")

public class R<T> implements Serializable {
    /**
     * 默认为空消息
     */
    private static final String DEFAULT_SUCCEED_CODE = "200";
    private static final String DEFAULT_FAILED_CODE = "500";
    private static final String DEFAULT_NULL_MESSAGE = "暂无承载数据";

    public R() {
    }

    public R(IResult result) {
        this.code = result.getCode();
        this.msg = result.getMessage();
        this.data = null;
    }

    @Override
    public String toString() {
        return JsonObject.mapFrom(this).toString();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private static final long serialVersionUID = 1L;

    //	@ApiModelProperty(value = "状态码", required = true)
    private String code;
    //	@ApiModelProperty(value = "返回消息", required = true)
    private String msg;
    //	@ApiModelProperty(value = "是否成功", required = true)
    private boolean success;
    //	@ApiModelProperty(value = "承载数据")
    private T data;

    public R(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.success = ServerCode.SUCCESS.getCode().equals(code);
        this.data = data;
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> R<T> data(T data) {
        return new R<>(ServerCode.SUCCESS.getCode(), ServerCode.SUCCESS.getMessage(), data);
    }


    /**
     * 返回R
     *
     * @param errorsMsg 错误信息
     * @return R
     */
    public static R<String> fail(String errorsMsg) {
        return new R<>(ServerCode.UNKNOWN_ERROR.getCode(), errorsMsg, null);
    }

    /**
     * 返回R
     *
     * @param result 错误码
     * @return R
     */
    public static R<String> fail(IResult result) {
        return new R<>(result);
    }


}
