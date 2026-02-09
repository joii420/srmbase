package com.step.web.runtime.core.verticle;

import com.step.api.runtime.core.BaseAsyncService;
import com.step.logger.LOGGER;
import com.step.web.runtime.core.utils.HotUtil;
import com.step.web.runtime.model.Deploy;
import com.step.web.runtime.register.VertxRegister;
import io.vertx.core.*;
import io.vertx.serviceproxy.ServiceBinder;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务注册到EventBus
 *
 * @author dtz
 */
@Singleton
public class AsyncRegistVerticle extends AbstractVerticle {

    @Inject
    io.vertx.mutiny.core.Vertx vertxMutiny;
    @Inject
    Instance<BaseAsyncService> listAyncService;
    @Inject
    VertxRegister register;
    @Inject
    HotUtil hotUtil;
//    @ConfigProperties

    @Override
    public void start(Promise<Void> startFuture) {
        ServiceBinder binder = new ServiceBinder(vertxMutiny.getDelegate());
        if (null != listAyncService) {
            List<Future> ftList = new ArrayList<>();
            List<Deploy> proxyClasses = listAyncService.stream().map(i -> {
                Class interfaceClass = Arrays.stream((i.getClass().getSimpleName().endsWith("_ClientProxy") ? i.getClass().getSuperclass() : i.getClass()).getInterfaces()).filter(c -> !c.getSimpleName().equals(BaseAsyncService.class.getSimpleName())).findFirst().get();
                return new Deploy(i, interfaceClass);
            }).collect(Collectors.toList());
            proxyClasses.forEach(deploy -> {
                Future ft = Future.future(x -> {
                });
                try {
                    String address = register.MICRO_GROUP + "." + register.REGISTRY_SERVE + "." + deploy.getInterFace().getSimpleName();
                    binder.setAddress(address).register(deploy.getInterFace(), deploy.getAsyncService()).completionHandler((Handler<AsyncResult<Void>>) ft);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ftList.add(ft);
            });
            CompositeFuture.all(ftList).onComplete(ar -> {
                if (ar.succeeded()) {
                    LOGGER.info("All async services registered");
                    startFuture.complete();
                } else {
                    LOGGER.error(ar.cause().getMessage());
                    startFuture.fail(ar.cause());
                }
            });
//                    .onSuccess(x -> {
//                if (x.succeeded()) {
//                    hotUtil.hotDeploy();
//                }
//            });
        }
    }
}
