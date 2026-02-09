package com.step.api.runtime.common;

/**
 * @author : Sun
 * @date : 2022/12/14  15:21
 */
public interface FilterPriority {
    /**
     * 过滤器优先级
     * 数字越大越先执行
     */
    /**
     * 环境过滤器
     */
    int REQUEST_LOG = 200;
    int GATEWAY_IP_VERIFY = 190;

    /**
     * token校验器
     */
    int GATEWAY_TOKEN_VERIFY = 180;
    /**
     * ip校验器
     */
    int SIGN_VERIFY = 98;
    /**
     * 环境过滤器
     */
    int CONTEXT = 99;
    /**
     * 请求路径校验器
     */
    int PATH_VERIFY = 97;


    /**
     * 参数校验器
     */
    int PARAM_VERIFY = 95;

    /**
     * 权限校验器
     */
    int AUTH_VERIFY = 94;


}
