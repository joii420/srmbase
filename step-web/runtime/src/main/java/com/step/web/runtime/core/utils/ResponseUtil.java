package com.step.web.runtime.core.utils;


import com.step.api.runtime.common.R;
import com.step.api.runtime.core.IResult;
import com.step.api.runtime.exception.Result;
import com.step.api.runtime.exception.ServerCode;
import com.step.api.runtime.utils.FormatUtil;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 异步线程禁止使用
 *
 * @author : Sun
 * @date : 2022/7/16  14:39
 */
public class ResponseUtil {

    public static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    private ResponseUtil() {
    }

    /**
     * 错误处理器
     */
    public static void response(RoutingContext rc, String errorsJson) {
        HttpUtil.fireJsonResponse(rc.response(), HttpServletResponse.SC_OK, R.fail(errorsJson));
    }

    /**
     * 错误处理器
     */
    public static void response(RoutingContext rc, IResult result) {
        HttpUtil.fireJsonResponse(rc.response(), HttpServletResponse.SC_OK, R.fail(result));
    }

    /**
     * 错误处理器
     */
    public static void responseResult(RoutingContext rc, IResult errorCode) {
        HttpUtil.fireJsonResponse(rc.response(), HttpServletResponse.SC_OK, FormatUtil.errorResultFormat(errorCode));
    }
    /**
     * 错误处理器
     */
    public static void responseResult(RoutingContext rc, List<Result> results) {
        HttpUtil.fireJsonResponse(rc.response(), HttpServletResponse.SC_OK, FormatUtil.errorResultFormat(results));
    }

    public static Throwable errorResult(RoutingContext rc, Throwable e) {
        HttpUtil.fireJsonResponse(rc.response(), HttpServletResponse.SC_OK, FormatUtil.errorResultFormat(List.of(new Result(Result.DEFAULT_ERROR_CODE, e.getMessage()))));

        return e;
    }

    public static Throwable error(Throwable e) {
        return error(AuthContext.getCTX(), e);
    }

    public static Throwable error(RoutingContext rc, Throwable e) {
        log.error("{} : {} {}", ServerCode.UNKNOWN_ERROR.getMessage(), e.getMessage(), e);
        response(rc, e.getMessage());
        return e;
    }

}
