package com.step.start.runtime.inject;

import com.step.api.runtime.core.CacheAPI;
import com.step.start.runtime.extension.ExtensionManager;
import io.quarkus.runtime.Startup;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author : Sun
 * @date : 2023/8/24  10:06
 */
@Singleton
@Startup
public class StaticInject {

    @Inject
    CacheAPI cacheAPI;

    private static StaticInject proxy;

    public StaticInject() {
        StaticInject.proxy = this;
    }

    public void init(@Observes StartupEvent startupEvent) {
        StaticInject.proxy = this;
    }

    public static CacheAPI cacheAPI() {
        return StaticInject.proxy.cacheAPI;
    }

}
