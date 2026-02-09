package com.step.mq.runtime.mqtt.model;

import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author : Sun
 * @date : 2023/8/3  9:55
 */
public class MqttMessage {

    private String topic;
    private String entityJson;

    public MqttMessage(String topic, String entityJson) {
        this.topic = topic;
        this.entityJson = entityJson;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEntityJson() {
        return entityJson;
    }

    public void setEntityJson(String entityJson) {
        this.entityJson = entityJson;
    }

}
