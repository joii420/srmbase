package com.step.web.runtime.core.start;


import com.step.web.runtime.core.verticle.AsyncRegistVerticle;
import com.step.web.runtime.core.verticle.ClusterAsyncRegistVerticle;
import com.step.web.runtime.register.VertxRegister;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mutiny.core.Vertx;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dtz
 */
@Singleton
public class VerticleDeployer {
    final Logger logger = LoggerFactory.getLogger("SERVICE-START");

    @Inject
    Vertx vertx;
    @Inject
    VertxRegister register;
    @Inject
    Router router;

    private static boolean promptMsg = false;
    final Verticle verticle;

    final DeploymentOptions deploymentOptions = new DeploymentOptions().setWorker(true);

    public VerticleDeployer(VertxRegister vertxRegister, AsyncRegistVerticle asyncRegistVerticle, ClusterAsyncRegistVerticle clusterAsyncRegistVerticle) {
        //初始化配置信息
        vertxRegister.initConfig();
        if (vertxRegister.IS_CLUSTER) {
            this.verticle = clusterAsyncRegistVerticle;
            this.deploymentOptions.setHa(true);
        } else {
            this.verticle = asyncRegistVerticle;
        }
    }

    public void init(@Observes StartupEvent startupEvent) {
        deployment(verticle, deploymentOptions);
    }

    private void deployment(Verticle verticle, DeploymentOptions deploymentOptions) {
        for (int i = 0; i < register.instanceSize; i++) {
            vertx.getDelegate().deployVerticle(verticle, deploymentOptions, ar -> {
                if (ar.succeeded()) {
                    if (!promptMsg) {
                        promptMsg = true;
                        //注册路由
                        router.route().handler(BodyHandler.create()) ;
                        logger.info("路由注册成功");
                        logger.info("启动成功");
                        printInstanceRun();
                    }
                } else {
                    if (!promptMsg) {
                        promptMsg = true;
                        logger.error("启动失败: {}", ar.cause());
                        printInstanceRun();
                    }
                }
            });
        }
    }

    private void printInstanceRun() {
        logger.info("instance " + verticle.getClass().getSimpleName());
    }

}