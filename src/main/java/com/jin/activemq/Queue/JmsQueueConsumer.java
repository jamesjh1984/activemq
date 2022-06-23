package com.jin.activemq.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/* 
    Queue: 1 --> 1
    1. Run JmsQueueProducer, means "send"
    2. Run JmsQueueConsumer, means "receive"
*/
public class JmsQueueConsumer {

    // 8161(管理端口)，61616(服务端口)，将8161改为61616，问题即可解决。
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String QUEUE_NAME = "queue.james.01";


    public static void main(String[] args) throws JMSException, IOException {

        // 按照给定的url,采用默认的username, password
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 获得连接，并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 创建会话， 两个参数: 事务，签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 创建目的地 (queue or topic)
        // Destination destination = session.createQueue(QUEUE_NAME);
        Queue queue = session.createQueue(QUEUE_NAME);

        // *****************************************
        // 创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);



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
                // TextMessage
                if(message != null && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        System.out.println("=== Consumer received queue message: [" + textMessage.getText() + "] ===");
//                        System.out.println("=== Consumer received queue message property: [" + textMessage.getStringProperty("c01") + "] ===");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

//                // MapMessage
//                if(message != null && message instanceof MapMessage) {
//                    MapMessage mapMessage = (MapMessage)message;
//                    try {
//                        System.out.println("=== Consumer received queue message: [" + mapMessage.getString("k1") + "] ===");
//                    } catch (JMSException e) {
//                        e.printStackTrace();
//                    }
//                }

            }
        });
        System.in.read(); // 保证控制台不灭，直到消费完了才关闭




        messageConsumer.close();
        session.close();
        connection.close();
    }
}
