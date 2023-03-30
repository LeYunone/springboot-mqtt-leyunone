package com.leyunone.springmqtt.subscribe;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName AbstractMqttCallback
 * @Author leyunone
 * @Date 2023-03-28
 * @Version
 * @Description mqtt回调抽象类型,并实现基本的重连等方法
 */
public abstract class AbstractMqttCallback implements MqttMessageCallback {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MqttAsyncClient mqttAsyncClient;

    private MqttAutoSubscribe mqttAutoSubscribe;

    public AbstractMqttCallback() {
    }

    @Override
    public void setMqttAsyncClient(MqttAsyncClient mqttAsyncClient) {
        this.mqttAsyncClient = mqttAsyncClient;
    }

    @Override
    public void setMqttAutoSubscribe(MqttAutoSubscribe mqttAutoSubscribe) {
        this.mqttAutoSubscribe = mqttAutoSubscribe;
    }

    /**
     * 重连
     * @param cause 连接丢失异常信息
     */
    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("mqtt client connection lost,exception message",cause);
        try {
            boolean connected = mqttAsyncClient.isConnected();
            if(connected){
                mqttAsyncClient.disconnect();
            }
            mqttAsyncClient.reconnect();
            connected = mqttAsyncClient.isConnected();
            while (!connected){
                connected = mqttAsyncClient.isConnected();
            }
            logger.info("mqtt client retry connect success");
            if(null != this.mqttAutoSubscribe){
                mqttAutoSubscribe.afterPropertiesSet();
            }
        }catch (Exception e){
            logger.error("mqtt client retry connect failed.exception message",e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("matt client is delivery...==");
    }

}
