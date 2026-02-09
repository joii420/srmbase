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


import com.step.api.runtime.core.IResultCode;

/**
 * 业务代码枚举
 *
 * @author Chill
 */

public enum ResultCode implements IResultCode {
	/** 请求返回码*/
	SUCCESS(0, "操作成功"),
	NOT_FOUND(404, "404 没找到请求"),
	UN_AUTHORIZED(401, "请求未授权"),
	FAILURE(400, "业务异常"),
	ERROR_PATH(1009901, "无效路径"),
	REQ_REFUSE(1009902, "请求被拒绝"),
	ERROR_LOGIN_STATE(1009903,"登录状态失效,请重新登录"),
	ERROR_USER_LOGIN_DATA(1009904,"用户信息验证失败..."),
	ERROR_PARAM(1009905, "错误参数"),
	ERROR_PARAM_TYPE(1009906,"错误参数类型"),
	ERROR_PARAM_EMPTY(1009907,"缺少必要参数"),
	ERROR_DATAKEY(1009908,"无效的dataKey信息"),
	;

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	ResultCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/** code编码 */
	final int code;
	/**
	 * 中文信息描述
	 */
	final String message;

}
