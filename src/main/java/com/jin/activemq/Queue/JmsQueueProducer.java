package com.jin.activemq.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/* 
    Queue: 1 --> 1
    1. Run JmsQueueProducer, means "send"
    2. Run JmsQueueConsumer, means "receive"
*/
public class JmsQueueProducer {

    // 8161(管理端口)，61616(服务端口)，将8161改为61616，问题即可解决。
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String QUEUE_NAME = "queue.james.01";


    public static void main(String[] args) throws JMSException {

        // 按照给定的url,采用默认的username, password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 获得连接，并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 创建会话， 两个参数: 事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 创建目的地 (queue or topic)
        Queue queue = session.createQueue(QUEUE_NAME);

        // *****************************************
        // 创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);

        // 设置Queue消息 持久化(DeliveryMode.PERSISTENT)或非持久化(DeliveryMode.NON_PERSISTENT)
        // 持久化(默认)：当服务器宕机，消息仍存在
        // 非持久化：当服务器宕机，消息不存在
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
//        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


        // 通过messageProducer生产的3条消息发送到MQ的queue里
        for (int i = 1; i <= 3; i++) {
            // 创建TextMessage
            TextMessage textMessage = session.createTextMessage("Queue_TextMessage_" + i);
            // setStringProperty
            textMessage.setStringProperty("c01", "vip");
            // 通过messageProducer发送给MQ
            messageProducer.send(textMessage);

            // 创建MapMessage
//            MapMessage mapMessage = session.createMapMessage();
//            mapMessage.setString("k1","Queue_MapMessage_v1");
//            messageProducer.send(mapMessage);
        }

        // 关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("=== Queue Message has sent to MQ. ===");
    }
}
