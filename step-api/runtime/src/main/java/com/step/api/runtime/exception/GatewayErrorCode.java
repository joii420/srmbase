package com.step.api.runtime.exception;

import com.step.api.runtime.core.IResultCode;

public enum GatewayErrorCode implements IResultCode {
    /**
     * code <= 999
     * 网关异常
     */
    INVALID_IP(1, "请求被拒绝: 非法ip!!!"),
    INVALID_USER(2, "请求未授权"),
    CONNECT_FAILED(3, "连接服务器异常"),
    INVALID_PATH(4, "无效的请求路径"),
    UNKNOWN_ERROR(5, "未知异常"),
    KEY_REPETITIVE(6, "请求key无法重复使用"),
    INVALID_TOKEN(7, "无效的token: token解析错误"),
    /**
     * 用户权限异常
     */
    UNAUTHORIZED_SITE(101, "无权访问据点"),
    SITE_EXPIRED(102, "据点访问权限已过期,请联系管理员"),
    UNAUTHORIZED_PROGRAM(103, "无权访问程序"),
    PROGRAM_EXPIRED(104, "程序访问权限已过期,请联系管理员"),
    UNAUTHORIZED_FUNCTION(105, "无权访问该功能"),
    TOKEN_EXPIRED(106, "登录过期,请重新登录"),
    /**
     * 500  请求参数异常
     */
    PARAM_EMPTY(501, "无效参数: null"),
    KEY_EMPTY(502, "无效请求key: null"),
    DATA_KEY_EMPTY(503, "无效参数dataKey: null"),
    DATA_KEY_ENT_EMPTY(504, "无效参数 dataKey: ent is null"),
    DATA_KEY_SITE_EMPTY(505, "无效参数 dataKey: site is null"),
    SERVICE_EMPTY(506, "无效参数 dataKey: site is null"),
    SERVICE_IP_EMPTY(507, "无效参数 service: ip is null"),
    SERVICE_ID_EMPTY(508, "无效参数 service: id is null"),
    SERVICE_NAME_EMPTY(509, "无效参数 service: name is null"),
    SERVICE_PROD_EMPTY(510, "无效参数 service: prod is null"),
    HOST_LANG_EMPTY(510, "无效参数 host: lang is null"),
    HOST_APPMOUDLE_EMPTY(511, "无效参数 host: appmoudle is null"),
    PAYLOAD_EMPTY(512, "无效参数 payload is null"),
    PAYLOAD_STD_DATA_EMPTY(513, "无效参数 payload: std_data is null"),
    PAYLOAD_STD_DATA_PARAMETER_EMPTY(514, "无效参数 payload - std_data: parameter is null"),
    ;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    GatewayErrorCode(int code, String message) {
        this.code = ErrorCode.GATEWAY_CODE + code;
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
