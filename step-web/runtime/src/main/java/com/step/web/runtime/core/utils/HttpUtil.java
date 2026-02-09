package com.step.web.runtime.core.utils;

import com.step.api.runtime.common.R;
import com.step.api.runtime.template.result.StepResult;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;


/**
 * Created by XHD on 2022/1/15.
 */
public class HttpUtil {

    public static void fireJsonResponse(HttpServerResponse response, int statusCode, R r) {
        response.putHeader("content-type", "application/json; charset=utf-8").setStatusCode(statusCode).end(r.toString());
    }

    public static void fireJsonResponse(HttpServerResponse response, int statusCode, StepResult erpResult) {
        response.putHeader("content-type", "application/json; charset=utf-8").setStatusCode(statusCode).end(erpResult.toString());
    }


    public static void fireJsonResponse(RoutingContext rx, int statusCode, StepResult erpResult) {
        fireJsonResponse(rx.response(), statusCode, erpResult);
    }


    public static void fireTextResponse(RoutingContext routingContext, String text) {
        routingContext.response().putHeader("content-type", "text/html; charset=utf-8").end(text);
    }

}
