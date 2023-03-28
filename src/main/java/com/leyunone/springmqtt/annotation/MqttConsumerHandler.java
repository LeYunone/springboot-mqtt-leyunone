package com.leyunone.springmqtt.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import javax.xml.stream.events.Comment;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2023-03-28
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttConsumerHandler {

    /**
     * bean对象名
     * @return
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
