package com.example.demo.config;


import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {

    @Autowired
    private Environment env;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setMaxMessagesPerTask(1);
        jmsListenerContainerFactory.setConcurrency(env.getProperty("jms.connections.concurrent"));
        jmsListenerContainerFactory.setSessionTransacted(env.getProperty("jms.connections.transacted", Boolean.class));
//        jmsListenerContainerFactory.setConcurrency("5-10");

        return jmsListenerContainerFactory;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ConnectionFactory connectionFactory){
        ActiveMQTopic activeMQ = new ActiveMQTopic(env.getProperty("jms.queue.out"));
        JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate(connectionFactory);
        jmsMessagingTemplate.setDefaultDestination(activeMQ);

        return jmsMessagingTemplate;
    }
}
