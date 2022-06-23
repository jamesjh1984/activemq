package com.jin.activemq.Spring;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/* 
    Queue: 1 --> 1
    1. Run SpringQueueProducer, means "send"
    2. Run SpringQueueConsumer, means "receive"
*/
@Service
public class SpringQueueProducer {

    @Autowired
    private JmsTemplate jmsTemplate;


    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        SpringQueueProducer springQueueProducer = (SpringQueueProducer) applicationContext.getBean("springQueueProducer");

        springQueueProducer.jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("Spring ActiveMQ Message.");
                return textMessage;
            }
        });

        System.out.println("=== Spring Queue Message has sent to MQ. ===");
    }
}
