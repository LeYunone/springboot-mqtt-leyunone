package com.leyunone.springmqtt.bean;

import java.lang.reflect.Method;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/7/22
 */
public class ConsumerHandler {

    private final Object beanName;

    private final Object beanObject;

    private final Method handleMethod;

    private final String pattern;

    public ConsumerHandler(Object beanName, Object beanObject, Method handleMethod, String pattern) {
        this.beanName = beanName;
        this.beanObject = beanObject;
        this.handleMethod = handleMethod;
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public Object getBeanName() {
        return beanName;
    }

    public Object getBeanObject() {
        return beanObject;
    }

    public Method getHandleMethod() {
        return handleMethod;
    }
}
