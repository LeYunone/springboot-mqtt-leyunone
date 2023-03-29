package com.leyunone.springmqtt.config;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2023-03-28
 */

import com.leyunone.springmqtt.handler.MqttMessageDispatchHandler;
import com.leyunone.springmqtt.handler.MultiHandlerDispatchHandler;
import com.leyunone.springmqtt.subscribe.MqttAutoSubscribe;
import com.leyunone.springmqtt.util.SslUtil;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Clock;

@Configuration
@ConditionalOnProperty(value = "spring.mqtt.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(MqttProperties.class)
public class MqttClientAutoConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(MqttClientAutoConfiguration.class);

    @Bean
    public MqttConnectOptions mqttConnectOptions(MqttProperties mqttProperties) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException {
        return getMqttConnectOption(mqttProperties);
    }

    private MqttConnectOptions getMqttConnectOption(MqttProperties mqttProperties) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttProperties.getUsername());
        options.setServerURIs(new String[]{mqttProperties.getUrl()});
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setCleanSession(true);
        options.setKeepAliveInterval(90);
        options.setAutomaticReconnect(true);
        options.setMaxInflight(10000);
        options.setConnectionTimeout(120);
        if (null != mqttProperties.getSsl()) {
            options.setSocketFactory(SslUtil.getSslSocktet(mqttProperties.getSsl()));
        }
//        options.setMqttVersion(MQTT_VERSION_3_1_1);
        return options;
    }

    @Bean
    public MqttAsyncClient mqttAsyncClient(MqttProperties mqttProperties, MqttConnectOptions mqttConnectOptions) {
        MqttAsyncClient sampleClient = null;
        try {
            sampleClient = new MqttAsyncClient(mqttProperties.getUrl(), mqttProperties.getClientId());
            sampleClient.connect(mqttConnectOptions);
            logger.info("mqtt client start connect");
            boolean successful = sampleClient.isConnected();
            long startTime = Clock.systemDefaultZone().millis();
            long timeout = mqttProperties.getTimeout() * 1000;
            long endTime = startTime;
            while (!successful && (endTime - startTime) <= timeout){
                Thread.sleep(10);
                successful = sampleClient.isConnected();
                endTime = Clock.systemDefaultZone().millis();
            }
            if(!successful){
                Thread.currentThread().interrupt();
                throw new RuntimeException("mqtt client connect is timeout");
            }
            logger.info("mqtt client connect is success.url: {},client id: {}",mqttProperties.getUrl(),mqttProperties.getClientId());
        }catch (Exception e){
            logger.error("mqtt client connect is failed. url: {},client id: {}",mqttProperties.getUrl(),mqttProperties.getClientId(),e);
        }
        
        return sampleClient;
        
    }


    @Bean
    public MqttMessageDispatchHandler mqttMessageDispatchHandler(){
        return new MultiHandlerDispatchHandler();
    }

    /**
     * 自动订阅
     * @param mqttProperties mqtt配置
     * @param mqttAsyncClient mqtt客户端
     * @return 订阅实现
     */
    @ConditionalOnMissingBean({MqttAutoSubscribe.class})
    @Bean
    public MqttAutoSubscribe mqttAutoSubscribe(MqttProperties mqttProperties,MqttAsyncClient mqttAsyncClient,MqttMessageDispatchHandler dispatchHandler){
        return new MqttAutoSubscribe(mqttProperties, mqttAsyncClient,dispatchHandler);
    }
}
