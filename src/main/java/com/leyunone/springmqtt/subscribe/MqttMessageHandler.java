package com.leyunone.springmqtt.subscribe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated {@link com.leyunone.springmqtt.annotation.MqttSubscribe}
 * @author leyunone
 * @Description 标识mqtt消息处理方法
 */
@Deprecated
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttMessageHandler {

    String topic() default "*";
}
