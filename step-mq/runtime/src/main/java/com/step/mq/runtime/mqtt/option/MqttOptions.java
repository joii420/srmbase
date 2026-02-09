package com.step.mq.runtime.mqtt.option;

import com.hivemq.client.mqtt.datatypes.MqttQos;

/**
 * @author : Sun
 * @date : 2024/1/4  16:07
 */
public class MqttOptions {

    private String charset;
    private MqttQos qos;

    public MqttOptions() {
        this.qos = MqttQos.AT_MOST_ONCE;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public MqttQos getQos() {
        return qos;
    }

    public void setQos(MqttQos qos) {
        this.qos = qos;
    }
}
