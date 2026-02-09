package com.step.api.runtime.exception;

import com.step.api.runtime.core.IResult;

/**
 * 所有服务中的异常
 */
public enum ServerCode implements IResult {
    /**
     * code <= 999
     * 网关异常
     */
    SUCCESS("0", "success"),
    UNKNOWN_ERROR("500", "request error"),
    UNAUTHORIZED_REQUEST("401", "unahthorized"),
    INVALID_IP("502", "Illegal IP"),
    INVALID_SIGN("403", "Illegal sign"),
    CONNECT_FAILED("402", "Connect error"),
    INVALID_PATH("404", "Invalid path"),
    KEY_REPETITIVE("405", "请求key无法重复使用"),

    /**
     * 用户权限异常
     */
    UNAUTHORIZED_SITE("101", "无权访问据点"),
    SITE_EXPIRED("102", "据点访问权限已过期,请联系管理员"),
    UNAUTHORIZED_PROGRAM("103", "无权访问程序"),
    PROGRAM_EXPIRED("104", "程序访问权限已过期,请联系管理员"),
    UNAUTHORIZED_FUNCTION("105", "无权访问该功能"),
    TOKEN_EXPIRED("106", "登录过期,请重新登录"),
    /**
     * 500  请求参数异常
     */
    PARAM_EMPTY("501", "Invalid param: null"),
    KEY_EMPTY("502", "无效请求key: null"),
    DATA_KEY_EMPTY("503", "Invalid paramdataKey: null"),
    DATA_KEY_ENT_EMPTY("504", "Invalid param dataKey: ent is null"),
    DATA_KEY_SITE_EMPTY("505", "Invalid param dataKey: site is null"),
    SERVICE_EMPTY("506", "Invalid param dataKey: site is null"),
    SERVICE_IP_EMPTY("507", "Invalid param service: ip is null"),
    SERVICE_ID_EMPTY("508", "Invalid param service: id is null"),
    SERVICE_NAME_EMPTY("509", "Invalid param service: name is null"),
    SERVICE_PROD_EMPTY("510", "Invalid param service: prod is null"),
    HOST_LANG_EMPTY("510", "Invalid param host: lang is null"),
    HOST_APPMOUDLE_EMPTY("511", "Invalid param host: appmoudle is null"),
    PAYLOAD_EMPTY("512", "Invalid param payload is null"),
    PAYLOAD_STD_DATA_EMPTY("513", "Invalid param payload: std_data is null"),
    PAYLOAD_STD_DATA_PARAMETER_EMPTY("514", "Invalid param payload - std_data: parameter is null"),
    EXECUTE_ERROR("515", "执行异常"),
    SQL_IS_EMPTY("515", "SQL为空"),
    TX_INFO_EMPTY("515", "userTransaction 参数不能为空"),
    DEPRECATED("515", "该方法已停用"),
    GET_RESULT_FAILED("516", "查询结果失败"),
    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    ServerCode(String code, String message) {
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
