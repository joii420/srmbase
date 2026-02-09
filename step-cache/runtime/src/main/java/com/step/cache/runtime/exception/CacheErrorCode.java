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
package com.step.cache.runtime.exception;


import com.step.api.runtime.core.IResultCode;
import com.step.api.runtime.exception.ErrorCode;

/**
 * 业务代码枚举
 *
 * @author Chill
 */

public enum CacheErrorCode implements IResultCode {
    /**
     * 请求返回码
     */
    KEY_EMPTY(1, "key is null"),
    VALUE_EMPTY(2, "value is null"),
    ;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    CacheErrorCode(int code, String message) {
        this.code = Integer.parseInt("" + ErrorCode.CACHE_CODE + code);
        this.message = message;
    }

    /**
     * code编码
     */
    final int code;
    /**
     * 中文信息描述
     */
    final String message;

}
