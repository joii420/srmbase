package com.step.web.runtime.core.verticle;


import com.step.api.runtime.core.BaseAsyncService;
import com.step.web.runtime.core.utils.BaseMicroserviceVerticle;
import com.step.web.runtime.core.utils.HotUtil;
import com.step.web.runtime.model.Deploy;
import com.step.web.runtime.register.VertxRegister;
import io.vertx.core.*;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dtz
 * 微服务服务注册到EventBus
 */
@Singleton
public class ClusterAsyncRegistVerticle extends BaseMicroserviceVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterAsyncRegistVerticle.class);

    @Inject
    io.vertx.mutiny.core.Vertx vertxMutiny;

    @Inject
    Instance<BaseAsyncService> listAyncService;
    @Inject
    HotUtil hotUtil;
    @Inject
    VertxRegister register;

    private Map<String, String> blackUrl = new HashMap<>();


    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        init();
        super.start();
        ServiceBinder binder = new ServiceBinder(vertxMutiny.getDelegate());
        if (null != listAyncService) {
            List<Future> ftList = new ArrayList<>();
            List<Deploy> proxyClasses = listAyncService.stream().map(i -> {
                Class interfaceClass = Arrays.stream((i.getClass().getSimpleName().endsWith("_ClientProxy") ? i.getClass().getSuperclass() : i.getClass()).getInterfaces()).filter(c -> !c.getSimpleName().equals(BaseAsyncService.class.getSimpleName())).findFirst().get();
                return new Deploy(i, interfaceClass);
            }).collect(Collectors.toList());
            register.initConfig();
            proxyClasses.forEach(deploy -> {
                Future ft = Future.future(x -> {
                });
                try {
                    //集群地址 + 服务地址 + 服务名称
                    String address = register.MICRO_GROUP + "." + register.REGISTRY_SERVE + "." + deploy.getInterFace().getSimpleName();
                    binder.setAddress(address).register(deploy.getInterFace(), deploy.getAsyncService()).completionHandler((Handler<AsyncResult<Void>>) ft);
                    if (blackUrl.containsKey(address) || blackUrl.isEmpty()) {
                        publishEventBusService(address, address, deploy.getInterFace());
                    }
                } catch (Exception e) {
                    System.out.println("ERROR_SERVICE: " + deploy.getAsyncService().getClass().getSimpleName());
                    e.printStackTrace();
                }
                ftList.add(ft);
            });
            CompositeFuture.all(ftList).onComplete(ar -> {
                if (ar.succeeded()) {
                    com.step.logger.LOGGER.info(LOGGER,"All async services registered");
                    startFuture.complete();
                } else {
                    com.step.logger.LOGGER.error(LOGGER,ar.cause().getMessage());
                    startFuture.fail(ar.cause());
                }
            });
//        onSuccess(x -> {
//                if (x.succeeded()) {
//                    hotUtil.hotDeploy();
//                }
//            }).onFailure(e-> com.step.logger.LOGGER.error(LOGGER,e,"部署失败: "));
//                    .onSuccess(x -> hotUtil.hotDeploy ());
        }
    }

//    private synchronized void hotDeploy() {
//        if (!this.hotDeploy) {
//            this.hotDeploy = true;
////            new Thread(() -> {
//            LOGGER.info("closing hot deploy !!!");
//            HotUtil.hotClose(hostConfig.getHost(), hostConfig.getPort());
////            }).start();
//        }
//    }

    private void init() {

    }
}
