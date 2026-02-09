package com.step.mq.runtime.mqtt.provider;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.step.logger.LOGGER;
import com.step.mq.runtime.mqtt.exception.MQException;
import com.step.mq.runtime.mqtt.model.MqttMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : Sun
 * @date : 2023/8/2  15:06
 */
public class MqttProducer {

    private volatile static Map<String, Mqtt5BlockingClient> senderManager = new HashMap<>();

    public MqttProducer() {
    }

    public static void sendMessage(String host, String topic, String message) {
        getSender(host, 1883)
                .toAsync()
                .publishWith()
                .topic(topic)
                .payload(message.getBytes())
                .qos(MqttQos.AT_MOST_ONCE)
                .send();
    }

    public static void sendMessage(String host, int port, String topic, String message) {
        getSender(host, port)
                .toAsync()
                .publishWith()
                .topic(topic)
                .payload(message.getBytes())
                .qos(MqttQos.AT_MOST_ONCE)
                .send();
    }

    public static void sendMessage(String host, int port, String topic, String message, MqttQos qos) {
        getSender(host, port)
                .toAsync()
                .publishWith()
                .topic(topic)
                .payload(message.getBytes())
                .qos(qos)
                .send();
    }

    private static Mqtt5BlockingClient getSender(String host, int port) {
        Mqtt5BlockingClient mqtt5BlockingClient = senderManager.get(getKey(host, port));
        if (mqtt5BlockingClient == null) {
            throw new MQException("消息通道未开启或已被关闭!");
        }
        return mqtt5BlockingClient;
    }

    private static String getKey(String host, int port) {
        String key = "SENDER_CLIENT##%s:%s";
        return String.format(key, host, port);
    }


    public static void closeSender(String host, int port) {
        Mqtt5BlockingClient client = senderManager.get(getKey(host, port));
        if (client != null) {
            client.disconnect();
            System.out.printf("%s:%s 关闭通道成功 %n", host, port);
        }
    }

    public static void closeSender(String host) {
        closeSender(host, 1883);
    }

    public synchronized static Mqtt5BlockingClient getOrCreateSender(String host, int port) {
        String key = getKey(host, port);
        Mqtt5BlockingClient mqtt5BlockingClient = senderManager.get(key);
        if (mqtt5BlockingClient == null) {
            mqtt5BlockingClient = Mqtt5Client.builder()
                    .identifier(UUID.randomUUID().toString())
                    .serverHost(host)
                    .serverPort(port)
                    .buildBlocking();
            mqtt5BlockingClient.connect();
            senderManager.put(key, mqtt5BlockingClient);
        }
        return mqtt5BlockingClient;
    }

    public synchronized static Mqtt5BlockingClient getOrCreateSender(String host) {
        return getOrCreateSender(host, 1883);
    }

}