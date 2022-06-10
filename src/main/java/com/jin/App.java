package com.jin;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;




/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws Exception {
        thread(new HelloProducer(), false);
        Thread.sleep(1000);
        thread(new HelloConsumer(), false);

//        thread(new HelloProducer(), false);
//        thread(new HelloConsumer(), false);
//        Thread.sleep(1000);
//        thread(new HelloConsumer(), false);
//        thread(new HelloProducer(), false);
//        thread(new HelloConsumer(), false);
//        thread(new HelloProducer(), false);
//        Thread.sleep(1000);
//        thread(new HelloConsumer(), false);
//        thread(new HelloProducer(), false);
//        thread(new HelloConsumer(), false);
//        thread(new HelloConsumer(), false);
//        thread(new HelloProducer(), false);
//        thread(new HelloProducer(), false);
    }



    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }



    public static class HelloProducer implements Runnable {
        @Override
        public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create a destination
                Destination destination = session.createQueue("James.queue2");

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create a message
                String text = "Hello! From: " + this.hashCode() + "_" + Thread.currentThread().getName();
                TextMessage message = session.createTextMessage(text);

                // Notify producer to send the message
                System.out.println("[Producer] Sent message: " + message.hashCode() + "_" + Thread.currentThread().getName());
                producer.send(message);

                // Clean up
                session.close();
                connection.close();


            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }

        }
    }



    public static class HelloConsumer implements Runnable, ExceptionListener {
        @Override
        public void run() {
            try {

                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                Connection connection = connectionFactory.createConnection();
                connection.start();

                connection.setExceptionListener(this);

                Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

                Destination destination = session.createQueue("James.queue2");

                MessageConsumer consumer = session.createConsumer(destination);

                Message message = consumer.receive(1000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("[Consumer] Received: " + text);
                } else {
                    System.out.println("[Consumer] Received: " + message);
                }


                consumer.close();
                session.close();
                connection.close();

            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }


        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured. Shutting down client.");
        }
    }

}
