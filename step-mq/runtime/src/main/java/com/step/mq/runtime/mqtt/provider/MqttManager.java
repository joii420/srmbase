package com.step.mq.runtime.mqtt.provider;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Sun
 * @date : 2024/4/11  13:41
 */
public class MqttManager {

    private static final Map<String, Mqtt5BlockingClient> SUBSCRIBER_MANAGER = new HashMap<>();

    public synchronized static Mqtt5BlockingClient save(String key, Mqtt5BlockingClient client) {
        Mqtt5BlockingClient exist = SUBSCRIBER_MANAGER.remove(key);
        if (exist != null) {
            exist.disconnect();
        }
        SUBSCRIBER_MANAGER.put(key, client);
        return client;
    }

    public synchronized static Mqtt5BlockingClient get(String key) {
        return SUBSCRIBER_MANAGER.get(key);
    }

    public synchronized static void close(String key) {
        Mqtt5BlockingClient remove = SUBSCRIBER_MANAGER.remove(key);
        if (remove != null) {
            remove.disconnect();
        }
    }
}
