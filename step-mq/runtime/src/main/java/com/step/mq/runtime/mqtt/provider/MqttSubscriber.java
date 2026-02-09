package com.step.mq.runtime.mqtt.provider;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopic;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.advanced.Mqtt5ClientAdvancedConfig;
import com.hivemq.client.mqtt.mqtt5.advanced.Mqtt5ClientAdvancedConfigBuilder;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.step.logger.LOGGER;
import com.step.mq.runtime.mqtt.interfaces.Subscriber;
import com.step.mq.runtime.mqtt.exception.MQException;
import com.step.mq.runtime.mqtt.model.MqttMessage;
import com.step.mq.runtime.mqtt.option.MqttOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author hf
 */
public class MqttSubscriber {
    public static final MqttOptions DEFAULT_OPTIONS;
    public static final Logger log = LoggerFactory.getLogger(MqttSubscriber.class);
    private static final Map<String, Mqtt5BlockingClient> SUBSCRIBER_MANAGER = new HashMap<>();

    public MqttSubscriber() {

    }

    private static Mqtt5BlockingClient getSubscriber(String host, int port, String topic) {
        String key = getKey(host, port, topic);
        Mqtt5BlockingClient mqtt5BlockingClient = SUBSCRIBER_MANAGER.get(key);
        if (mqtt5BlockingClient == null) {
            mqtt5BlockingClient = createSubscriber(host, port, topic);
        }
        return mqtt5BlockingClient;
    }

    private synchronized static Mqtt5BlockingClient createSubscriber(String host, int port, String topic) {
        String key = getKey(host, port, topic);
        Mqtt5BlockingClient mqtt5BlockingClient = SUBSCRIBER_MANAGER.get(key);

        if (mqtt5BlockingClient == null) {
            mqtt5BlockingClient = Mqtt5Client.builder()
                    .identifier(UUID.randomUUID().toString())
                    .serverHost(host)
                    .serverPort(port)
//                    .advancedConfig(Mqtt5ClientAdvancedConfig.builder().build())
//                    .automaticReconnect(MqttClientAutoReconnect.builder().build())
                    .buildBlocking();
            mqtt5BlockingClient.connect();
            SUBSCRIBER_MANAGER.put(key, mqtt5BlockingClient);
            LOGGER.info(log, "%s:%s$%s Subscriber init success!", host, port, topic);
        }
        return mqtt5BlockingClient;
    }

    public static void startSubscriber(String host, int port, String topic, Consumer<MqttMessage> consumer) {
        startSubscriber(host, port, topic, consumer, DEFAULT_OPTIONS);
    }

    public static void startSubscriber(String host, int port, String topic, Consumer<MqttMessage> consumer, MqttOptions options) {
        getSubscriber(host, port, topic)
                .toAsync()
                .subscribeWith()
                .topicFilter(topic)
                .qos(options.getQos())
                .callback(publish -> handleMessage(publish, consumer, options))
                .send();
        LOGGER.info(log, "%s:%s$%s Topic init success!", host, port, topic);
    }

    public static void startSubscriber(Subscriber<?> subscriber) {
        getSubscriber(subscriber.getHost(), subscriber.getPort(), subscriber.getTopic()).toAsync().subscribeWith()
                .topicFilter(subscriber.getTopic())
                .qos(subscriber.getOps())
                .callback(subscriber::handlerMessage)
                .send();
        LOGGER.info(log, "%s:%s$%s 初始化成功", subscriber.getHost(), subscriber.getPort(), subscriber.getTopic());
    }

    public static void startSubscriber(String host, String topic, Consumer<MqttMessage> consumer) {
        startSubscriber(host, 1883, topic, consumer);
    }

    private static void handleMessage(Mqtt5Publish publish, Consumer<MqttMessage> consumer, MqttOptions options) {
        ByteBuffer byteBuffer = publish.getPayload().orElseThrow(() -> new MQException("Message is empty!"));
        // 根据实际编码进行选择
//        String message = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        String message = Charset.forName(options.getCharset()).decode(byteBuffer).toString();
        MqttTopic topic = publish.getTopic();
        MqttMessage mqttMessage = new MqttMessage(topic.toString(), message);
        consumer.accept(mqttMessage);
    }

    private static String getKey(String host, int port, String topic) {
        String key = "SUBSCRIBER_CLIENT##%s:%s$%s";
        return String.format(key, host, port, topic);
    }

    public static void closeSubscriber(String host, int port, String topic) {
        Mqtt5BlockingClient client = SUBSCRIBER_MANAGER.remove(getKey(host, port, topic));
        if (client != null) {
            client.disconnect();
            LOGGER.info(log, "%s:%s$%s Subscriber close success!", host, port, topic);
        }
    }

    public static void closeSubscriber(String host, String topic) {
        closeSubscriber(host, 1883, topic);
    }

    static {
        DEFAULT_OPTIONS = new MqttOptions();
        DEFAULT_OPTIONS.setCharset("UTF-8");
    }

}
