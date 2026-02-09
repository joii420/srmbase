package com.step.cache.runtime.config;

import com.step.api.runtime.core.CacheAPI;
import com.step.cache.runtime.ignite.support.IgniteCacheSupport;
import com.step.tool.utils.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class CacheConfiguration {

    @ConfigProperty(name = "quarkus.cache.name", defaultValue = "igniteCache")
    String cacheName;

    @ConfigProperty(name = "quarkus.cache.host.address", defaultValue = "127.0.0.1:10800")
    String address;
    @ConfigProperty(name = "quarkus.cache.password", defaultValue = "null")
    String password;
    @ConfigProperty(name = "quarkus.cache.open", defaultValue = "true")
    String open;

    @Produces
    public CacheAPI initCache(IgniteClient ignite) {
        if ("false".equals(open)) {
            return new NoCacheSupport();
        }
        return new IgniteCacheSupport(ignite, cacheName);
    }

    /**
     * 需要重写ClientConfiguration配置文件
     *
     * @return
     */
    @Produces
    public IgniteClient initIgniteClient() {
        if ("false".equals(open)) {
            return null;
        }
        ClientConfiguration cfg = new ClientConfiguration().setAddresses(address);
        if (StringUtil.isNotEmpty(password)) {
            cfg.setUserPassword(password);
        }
        //心跳包
        cfg.setHeartbeatEnabled(true);
        cfg.setHeartbeatInterval(5000);
        cfg.setRetryLimit(5);
        cfg.setTimeout(3000);
        //自动序列化对象
//        cfg.setAutoBinaryConfigurationEnabled(true);
        return Ignition.startClient(cfg);
    }
}