package com.jin.activemq.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/* 
    Topic: 1 --> many
    1. Run JmsTopicConsumerPersist, means "subscribe first"
    2. Run JmsTopicProducerPersist, means "publish then"
*/
public class JmsTopicConsumerPersist_A1 {

    // 8161(管理端口)，61616(服务端口)，将8161改为61616，问题即可解决。
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String TOPIC_NAME = "persistent_topic.james.01";


    public static void main(String[] args) throws JMSException, IOException {

        System.out.println("******** A1 ********");

        // 按照给定的url,采用默认的username, password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 获得连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("A1");


        // 创建会话， 两个参数: 事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 创建目的地 (queue or topic)
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic,"remark...");

        // 启动
        connection.start();

        // *****************************************
        // 创建Topic Subscriber
        Message message = topicSubscriber.receive();

        while (message != null) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Received persistent topic: " + textMessage.getText());
            message = topicSubscriber.receive(1000L); // 收到消息之后offline的时间
        }


        session.close();
        connection.close();
    }
}
