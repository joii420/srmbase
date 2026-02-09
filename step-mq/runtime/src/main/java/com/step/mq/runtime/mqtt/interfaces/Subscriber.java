package com.step.mq.runtime.mqtt.interfaces;

import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.step.mq.runtime.mqtt.exception.MQException;
import com.step.tool.utils.JsonUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author : Sun
 * @date : 2023/8/3  8:19
 */
public interface Subscriber<T> extends Mqtt {
    /**
     * 设置协议
     */
    Class<T> getAgreement();

    /**
     * 消息处理
     */
    void handler(T entity);

    default void handlerMessage(Mqtt5Publish publish) {
        ByteBuffer byteBuffer = publish.getPayload().orElseThrow(() -> new MQException("Message is empty!"));
        // 根据实际编码进行选择
        String message = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        T entity;
        try {
            entity = JsonUtil.parse(message, getAgreement());
        } catch (Exception e) {
            throw new MQException("无法识别的协议!");
        }
        handler(entity);
    }

}
