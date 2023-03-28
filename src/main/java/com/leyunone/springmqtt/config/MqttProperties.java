package com.leyunone.springmqtt.config;

import com.sun.istack.internal.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2023-03-28
 */
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties {

    private String url;

    private String clientId = "mqtt-sdk-client";

    private String username;

    private String password;

    private String group = "default-group";

    private int keepalive;

    private String ssl;

    private List<MqttTopic> topics = new ArrayList<>();


    static class MqttTopic {

        @NotNull
        private String topic;

        private int qos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public List<MqttTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<MqttTopic> topics) {
        this.topics = topics;
    }
}
