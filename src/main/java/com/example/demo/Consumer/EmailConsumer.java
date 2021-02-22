package com.example.demo.Consumer;

import com.example.demo.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;


@Component
public class EmailConsumer {
    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);


    @JmsListener(destination = "emailqueue", containerFactory = "EmailFactory")
    public void messsageListener(Email email)
    {
        logger.info("Message "+ email);
    }


    @Bean("EmailFactory")
    public DefaultJmsListenerContainerFactory myfactory(ConnectionFactory connectionFactory,
                                                           DefaultJmsListenerContainerFactoryConfigurer containerFactory){

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        System.out.println("factory: "+factory);
        containerFactory.configure(factory, connectionFactory);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.OBJECT);
        converter.setTypeIdPropertyName("emailinfo");

        factory.setMessageConverter(converter);
        return factory;
    }



}
