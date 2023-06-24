package com.leyunone.springmqtt.api;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * :)
 *  mqtt-emqx 的api
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2023-06-24
 */
@Service
public class MqttSubscribeService {

    @Autowired
    private MqttAsyncClient mqttAsyncClient;
    
    /**
     * 校验mqtt连接状态
     */
    private void validMqttConnection(){
        boolean connected = mqttAsyncClient.isConnected();
        if(!connected) throw new RuntimeException("mqtt连接断开");
    }

    /**
     * 正则表达式获取目标客户端连接状态
     * @param pattern
     */
    public void getClient(String pattern) {
        
    }
}
