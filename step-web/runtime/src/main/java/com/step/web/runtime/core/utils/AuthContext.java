package com.step.web.runtime.core.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
@Deprecated
public class AuthContext {
    private static TransmittableThreadLocal<RoutingContext> CTX = new TransmittableThreadLocal<>();

    public static void setCTX(RoutingContext ctxData) {
        CTX.set(ctxData);
    }

    public static RoutingContext getCTX() {
        return CTX.get();
    }

    public static HttpServerRequest getRequest() {
        return getCTX().request();
    }

    public static void clear() {
        CTX.remove();
    }

}
