package com.step.web.runtime.core.utils;


import com.step.api.runtime.annotation.MicroServer;
import com.step.web.runtime.register.VertxRegister;
import io.quarkus.runtime.Startup;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.serviceproxy.ServiceProxyBuilder;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author dtz
 */
@Singleton
@Startup
public final class AsyncServiceUtil {

    @Inject
    Vertx vertx;
    @Inject
    VertxRegister register;

    private static AsyncServiceUtil proxy;

    private AsyncServiceUtil() {
        AsyncServiceUtil.proxy = this;
    }

    public static <T> T getAsyncServiceInstance(Class<T> asClazz, Vertx vertx) {
        String address = getAddress(asClazz);
        return new ServiceProxyBuilder(vertx.getDelegate()).setAddress(address).build(asClazz);
    }

    public static <T> T getAsyncServiceInstance(Class<T> asClazz) {
        String address = getAddress(asClazz);
//        String address = asClazz.getName();
        return (new ServiceProxyBuilder(AsyncServiceUtil.proxy.vertx.getDelegate())).setAddress(address).setOptions(new DeliveryOptions().setSendTimeout(60000)).build(asClazz);
    }

    public static <T> T getAsyncServiceInstance(String address, Class<T> asClazz) {
        return (new ServiceProxyBuilder(AsyncServiceUtil.proxy.vertx.getDelegate())).setAddress(address).build(asClazz);
    }

    private static <T> String getAddress(Class<T> asClazz) {
        AsyncServiceUtil.proxy.register.initConfig();
        return getAddress(asClazz, AsyncServiceUtil.proxy.register);
    }

    /**
     * 获取服务地址
     *
     * @param asClazz
     * @param vertxRegister
     * @param <T>
     * @return
     */
    private static <T> String getAddress(Class<T> asClazz, VertxRegister vertxRegister) {
        MicroServer microServer = asClazz.getDeclaredAnnotation(MicroServer.class);
        String serve = vertxRegister.REGISTRY_SERVE;
        if (vertxRegister.IS_CLUSTER && microServer != null && !"".equals(microServer.value().getServerName())) {
            serve = microServer.value().getServerName();
        }
        return vertxRegister.MICRO_GROUP + "." + serve + "." + asClazz.getSimpleName();
    }

}

