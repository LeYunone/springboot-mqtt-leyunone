package com.leyunone.springmqtt.subscribe;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;

/**
 * @ClassName MqttMessageCallback
 * @Author leyunone
 * @Date 2023-03-28
 * @Version
 * @Description
 */
public interface MqttMessageCallback extends MqttCallback {

    /**
     * 设置mqtt消费者用于重连后的重新订阅
     * @param mqttAutoSubscribe
     */
    void setMqttAutoSubscribe(MqttAutoSubscribe mqttAutoSubscribe);

    /**
     * 设置客户端
     * @param mqttAsyncClient
     */
    void setMqttAsyncClient(MqttAsyncClient mqttAsyncClient);


}
