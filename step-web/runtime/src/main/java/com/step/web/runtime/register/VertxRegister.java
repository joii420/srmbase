package com.step.web.runtime.register;

import com.step.api.runtime.common.MicroRegister;
import com.step.api.runtime.utils.HostConfig;
import com.step.logger.LOGGER;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import java.util.List;

/**
 * @author : Sun
 * @date : 2023/1/9  16:33
 */
@Singleton
public class VertxRegister {

    public static final Logger log = LoggerFactory.getLogger("VERTX-START");
    public final static String LOCAL = MicroRegister.LOCAL.getServerName();
    private final static List<String> validServer = MicroRegister.micros();
    /**
     * 实例数量
     */
    @ConfigProperty(name = "quarkus.vertx.instanceSize", defaultValue = "3")
    public int instanceSize;
    /**
     * 服务名
     */
    @ConfigProperty(name = "quarkus.vertx.cluster.server-name", defaultValue = "DEV")
    public String REGISTRY_SERVE;
    /**
     * 服务名
     */
    @ConfigProperty(name = "quarkus.vertx.cluster.group", defaultValue = "AUTO")
    public String MICRO_GROUP;
    /**
     * 微服务启动开关
     */
    @ConfigProperty(name = "quarkus.vertx.cluster.clustered", defaultValue = "false")
    public boolean IS_CLUSTER;


    private boolean isInit = false;

    public boolean isDev = false;


    public VertxRegister() {
    }

    /**
     * 初始化
     */
    public void initConfig() {
        if (!isInit) {
            if (IS_CLUSTER) {
                if (MicroRegister.DEV.getServerName().equals(REGISTRY_SERVE)) {
                    LOGGER.info("当前模式: 开发者模式");
                    isDev = true;
                    REGISTRY_SERVE = HostConfig.localHost();
                } else if (!validServer.contains(REGISTRY_SERVE)) {
                    log.warn("未注册的服务: {}", REGISTRY_SERVE);
                    REGISTRY_SERVE = LOCAL;
                    IS_CLUSTER = false;
                }
            } else {
                LOGGER.info("当前模式: 本地模式");
                REGISTRY_SERVE = LOCAL;
            }
            isInit = true;
        }
    }


}
