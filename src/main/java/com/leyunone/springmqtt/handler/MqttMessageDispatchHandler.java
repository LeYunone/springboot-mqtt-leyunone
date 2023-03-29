package com.leyunone.springmqtt.handler;

import com.leyunone.springmqtt.subscribe.AbstractMqttCallback;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 消息调度处理器
 * @author leyunone
 * @date 2023-03-28
**/
public abstract class MqttMessageDispatchHandler extends AbstractMqttCallback implements ApplicationContextAware {

    protected ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
