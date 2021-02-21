package com.example.demo.Consumer;

import com.example.demo.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class EmailConsumer {
    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);

    @JmsListener(destination = "emailqueue")
    public void messsageListener(Email email)
    {
        logger.info("Message "+ email.getDestination());
    }


}
