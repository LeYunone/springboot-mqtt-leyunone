package com.leyunone.springmqtt.handler;

import com.leyunone.springmqtt.annotation.MqttConsumerHandler;
import com.leyunone.springmqtt.annotation.MqttSubscribe;
import com.leyunone.springmqtt.bean.ConsumerHandler;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author leyunone
 * @date 2023-03-28
 **/
public class MultiHandlerDispatchHandler extends MqttMessageDispatchHandler implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(MultiHandlerDispatchHandler.class);

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        if (consumerHandlers.containsKey(topic)) {
            ConsumerHandler consumerHandler = consumerHandlers.get(topic);
            try {
                consumerHandler.getHandleMethod().invoke(consumerHandler.getBeanObject(), topic, mqttMessage);
                logger.debug("The data of this topic {} is consumed by this handler {}", topic, consumerHandler.getBeanName());

            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("An exception occurred when calling the handler method,handler {} method {}", consumerHandler.getBeanName(), consumerHandler.getHandleMethod().getName());
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> handlerArray = context.getBeansWithAnnotation(MqttConsumerHandler.class);
        if (handlerArray.size() == 0) {
            throw new RuntimeException("mqtt consumer loading fail because consumer is empty.");
        }
        handlerArray.forEach((k, v) -> {
            Class<?> aClass = v.getClass();
            Method[] methods = aClass.getMethods();
            if (methods.length == 0) {
                throw new RuntimeException("mqtt consumer loading fail because consumer is empty.");
            }
            Method mqttMessageHandlerMethod = null;
            for (Method method : methods) {
                MqttSubscribe annotation = method.getAnnotation(MqttSubscribe.class);
                if (null != annotation) {
                    mqttMessageHandlerMethod = method;
                    break;
                }
            }
            Objects.requireNonNull(mqttMessageHandlerMethod, "No consumption method found on this handler " + k);
            MqttSubscribe annotation = AnnotationUtils.getAnnotation(mqttMessageHandlerMethod, MqttSubscribe.class);
            Objects.requireNonNull(annotation, "Method is missing necessary annotations " + mqttMessageHandlerMethod.getName());
            String pattern = annotation.topic();

            ConsumerHandler consumerHandler = new ConsumerHandler(k, v, mqttMessageHandlerMethod, pattern);
            consumerHandlers.put(pattern, consumerHandler);
        });
    }
}
