package com.step.api.runtime.utils;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Singleton;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : Sun
 * @date : 2022/12/31  15:27
 */
@Singleton
public class HostConfig {
    /**
     * 服务ip
     */
    @ConfigProperty(name = "quarkus.http.host", defaultValue = "127.0.0.1")
    public String host;
    /**
     * 服务端口
     */
    @ConfigProperty(name = "quarkus.http.port", defaultValue = "8080")
    public String port;

    public HostConfig() {
    }

    public String getHost() {
        return host;
    }


    public String getPort() {
        return port;
    }

    public static String localHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


}
