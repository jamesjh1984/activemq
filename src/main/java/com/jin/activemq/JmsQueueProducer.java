package com.jin.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

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

        // 通过messageProducer生产的3条消息发送到MQ的queue里
        for (int i = 1; i <= 3; i++) {
            // 创建消息
            TextMessage textMessage = session.createTextMessage("Queue_Message_" + i);
            // 通过messageProducer发送给MQ
            messageProducer.send(textMessage);
        }

        // 关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("=== Queue Message has sent to MQ. ===");
    }
}
