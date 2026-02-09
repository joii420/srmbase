package com.step.mq.runtime.mqtt.interfaces;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.step.mq.runtime.mqtt.exception.MQException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author : Sun
 * @date : 2023/8/3  8:26
 */
public interface Mqtt {

    default String getHost() {
        return "localhost";
    }

    default int getPort() {
        return 1883;
    }

    default MqttQos getOps() {
        return MqttQos.AT_LEAST_ONCE;
    }

    String getTopic();


}
