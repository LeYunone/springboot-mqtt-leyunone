package com.leyunone.springmqtt.test;

import com.leyunone.springmqtt.annotation.MqttConsumerHandler;
import com.leyunone.springmqtt.annotation.MqttSubscribe;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@MqttConsumerHandler
@Component
public class MqttMessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(MqttMessageConsumer.class);
    
    @MqttSubscribe(topic = "指定的topic主题")
    public void messageAccept(String topic, MqttMessage message) {
        logger.info("MQTT message receive topic:{},message:{}", topic, message.toString());
    }
}
