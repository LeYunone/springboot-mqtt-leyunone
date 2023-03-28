package com.leyunone.springmqtt.config;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2023-03-28
 */

import com.leyunone.springmqtt.util.SslUtil;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
@ConditionalOnProperty(value = "spring.mqtt.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MqttProperties.class)
public class MqttClientAutoConfiguration {

    @Bean
    public MqttConnectOptions mqttConnectOptions(MqttProperties mqttProperties) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        return getMqttConnectOption(mqttProperties);
    }

    private MqttConnectOptions getMqttConnectOption(MqttProperties mqttProperties) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttProperties.getUsername());
        options.setServerURIs(new String[] { mqttProperties.getUrl() });
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(90);
        options.setAutomaticReconnect(true);
        options.setMaxInflight(10000);
        options.setConnectionTimeout(120);
        if(null != mqttProperties.getSsl()){
            options.setSocketFactory(SslUtil.getSslSocktet(mqttProperties.getSsl()));
        }
//        options.setMqttVersion(MQTT_VERSION_3_1_1);
        return options;
    }
}
