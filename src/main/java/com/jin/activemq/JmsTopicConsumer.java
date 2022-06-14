package com.jin.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/* 
    Topic: 1 --> many
    1. Run JmsTopicConsumer, means "subscribe first"
    2. Run JmsTopicProducer, means "publish then"
*/
public class JmsTopicConsumer {

    // 8161(管理端口)，61616(服务端口)，将8161改为61616，问题即可解决。
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String TOPIC_NAME = "topic.james.01";


    public static void main(String[] args) throws JMSException, IOException {

        // 按照给定的url,采用默认的username, password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 获得连接，并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 创建会话， 两个参数: 事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 创建目的地 (queue or topic)
        Topic topic = session.createTopic(TOPIC_NAME);

        // *****************************************
        // 创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);



//        方法一，receive(): 同步阻塞方式
//              receive(4000L): 等待指定时间，过时不候
//        while (true) {
////            TextMessage textMessage = (TextMessage)messageConsumer.receive();
//            TextMessage textMessage = (TextMessage)messageConsumer.receive(4000L);
//            if (textMessage != null) {
//                System.out.println("=== Consumer has received message: [" + textMessage.getText() + "] ===");
//            } else {
//                break;
//            }
//        }



//        方法二，监听的方式消费消息
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message != null && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println("=== Consumer has received topic message: [" + textMessage.getText() + "] ===");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        Lamda表达式， jdk1.8不支持
/*        messageConsumer.setMessageListener((message) -> {
            if(message != null && message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println("=== Consumer has received topic message: [" + textMessage.getText() + "] ===");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });*/

        System.in.read(); // 保证控制台不灭，直到消费完了才关闭




        messageConsumer.close();
        session.close();
        connection.close();
    }
}
