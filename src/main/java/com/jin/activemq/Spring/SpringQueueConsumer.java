package com.jin.activemq.Spring;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.io.IOException;

/* 
    Queue: 1 --> 1
    1. Run SpringQueueProducer, means "send"
    2. Run SpringQueueConsumer, means "receive"
*/
@Service
public class SpringQueueConsumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        SpringQueueConsumer springQueueConsumer = (SpringQueueConsumer) applicationContext.getBean("springQueueConsumer");

        String textMessage = (String) springQueueConsumer.jmsTemplate.receiveAndConvert();

        System.out.println("=== Consumer received Spring queue message: [" + textMessage + "] ===");

    }
}
