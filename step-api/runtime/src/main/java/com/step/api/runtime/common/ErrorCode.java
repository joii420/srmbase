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

/**
 * 业务代码枚举
 *
 * @author Chill
 */

public enum ErrorCode implements IResult {
    /**
     * 请求返回码
     */
    SUCCESS("400", "操作成功"),

    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * code编码
     */
    final String code;
    /**
     * 中文信息描述
     */
    final String message;

}
