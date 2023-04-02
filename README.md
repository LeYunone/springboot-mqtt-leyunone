# spring-mqtt

基于Springboot封装了paho-mqtt原生客户端。


*  封装了mqtt连接和客户端创建，创建后并加载到容器中
*  封装了MqttCallback的实现，使用者可以使用`@MqttConsumerHandler`和`@MqttMessageHandler`注解实现消息接收

## 接入方式
### 注意事项

*  接入前提需要引入Springboot-starter
### 接入步骤
1. 导入spring-mqtt-starter包
```
<dependency>
    <groupId>com.gvs.iot</groupId>
    <artifactId>spring-mqtt-starter</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```
2. 如果你需要订阅消息，创建你的消费者，并加载到Spirngioc中，使用`MqttConsumerHandler`来标识消费者实例，
使用`@MqttMessageHandler`注解来标识消息处理方法，并提供`topic`和`message`两个参数
```
@Component
@MqttConsumerHandler
public class MqttConsumer {

    final static Logger logger = LoggerFactory.getLogger(MqttConsumer.class);

    @MqttMessageHandler
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        logger.debug("message arrived. topic:{},message:{}.",topic,message.toString());
    }


}
```
3. 在yml里配置mqtt的连接信息
```
spring:
  mqtt:
    url: 连接地址
    clientId: 客户端Id
    topics:
      - topic: topic
        qos: qos
      - topic: topic
        qos: qos
    username: 用户名称
    password: 密码
    group: 分组
    ssl:  ssl ca证书地址
```

经过以上3个步骤就成功接入了，启动Springboot项目看到控制台打印mqtt客户端启动成功，就是启动成功了。

具体实现可以阅读代码。
