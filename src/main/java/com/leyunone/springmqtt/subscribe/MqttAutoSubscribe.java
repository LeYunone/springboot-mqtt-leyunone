package com.leyunone.springmqtt.subscribe;

import com.leyunone.springmqtt.config.MqttProperties;
import com.leyunone.springmqtt.handler.MqttMessageDispatchHandler;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leyunone
 */
public class MqttAutoSubscribe implements InitializingBean {

    final static Logger logger = LoggerFactory.getLogger(MqttAutoSubscribe.class);

    private static final String GROUP_SUBSCRIBE_PREFIX = "$share/";

    private static final String DEFAULT_SUBSCRIBE_PREFIX = "$queue/";

    final private MqttProperties mqttProperties;

    final private MqttAsyncClient mqttAsyncClient;

    final private MqttMessageDispatchHandler dispatchHandler;

    public MqttAutoSubscribe(MqttProperties mqttProperties, MqttAsyncClient mqttAsyncClient, MqttMessageDispatchHandler dispatchHandler) {
        this.mqttProperties = mqttProperties;
        this.mqttAsyncClient = mqttAsyncClient;
        this.dispatchHandler = dispatchHandler;
    }


    @Override
    public void afterPropertiesSet() {
        if (Thread.interrupted()) {
            return;
        }
        List<MqttProperties.MqttTopic> mqttTopics = mqttProperties.getTopics();
        if (CollectionUtils.isEmpty(mqttTopics)) {
            logger.warn("subscribe topic is blank.subscribe stop");
            return;
        }
        String[] topics = mqttTopics.stream().map(mqttTopic -> {
            String group = mqttProperties.getGroup();
            String topic = mqttTopic.getTopic();
            if (!StringUtils.isEmpty(group)) {
                return GROUP_SUBSCRIBE_PREFIX + group + "/" + topic;
            } else {
                return DEFAULT_SUBSCRIBE_PREFIX + topic;
            }
        }).collect(Collectors.toList()).toArray(new String[mqttTopics.size()]);
        int[] qos = mqttTopics.stream().map(MqttProperties.MqttTopic::getQos).mapToInt(Integer::intValue).toArray();
        try {
            dispatchHandler.setMqttAutoSubscribe(this);
            dispatchHandler.setMqttAsyncClient(mqttAsyncClient);
            mqttAsyncClient.setCallback(dispatchHandler);
            mqttAsyncClient.subscribe(topics, qos);
            logger.info("subscribe success topic:{}", Arrays.toString(topics));
        } catch (Exception e) {
            logger.error("subscribe failed...", e);
        }
    }


}
