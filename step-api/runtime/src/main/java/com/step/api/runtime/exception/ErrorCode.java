package com.step.api.runtime.exception;

/**
 * @author : Sun
 * @date : 2022/12/15  8:32
 */
public interface ErrorCode {

    /**
     * 核心异常
     */
    int CORE_CODE = 1;
    /**
     * 网关异常
     */
    int GATEWAY_CODE = 10000;
    /**权限异常*/
    int AUTH_CODE = 20000;
    /**缓存异常*/
    int CACHE_CODE= 30000;
    /**操作异常*/
    int OPERA_CODE= 40000;



    /**低代码平台*/
    int SYSTEM_CODE = 1000000;
}
