package com.leyunone.springmqtt.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2023-03-30
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttSubscribe {

    @AliasFor("topic")
    String topic() default "\\.*.*";

    @AliasFor("value")
    String value() default "";
}
