package com.jin.activemq.MQBroker;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

public class MQBroker {

    public static void main(String[] args) throws Exception {

        // 嵌入式MQ Broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");


//        BrokerService brokerService = BrokerFactory.createBroker(new URI("broker:tcp://localhost:61616"));

        brokerService.start();

        System.in.read(); // 保证控制台不灭

    }

}
