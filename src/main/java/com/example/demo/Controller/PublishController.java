package com.example.demo.Controller;

import com.example.demo.model.Email;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.Enumeration;
import java.util.List;

@RequestMapping("/Queue")
@RestController
public class PublishController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Environment env;


    @PostMapping("/PublishEmail")
    public ResponseEntity<String> publishMessages(@RequestBody Email email){
        try{
            jmsTemplate.convertAndSend("emailqueue", email);

            return new ResponseEntity<String>("Sent Seccessfully",HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity<String>(e.getMessage().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ProcessQueue")
    public void processqueue() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("emailqueue");
        System.out.println("queue: "+queue);
        MessageConsumer consumer = session.createConsumer(queue);
        System.out.println("Start");
        TextMessage textMsg = (TextMessage) consumer.receive();
        System.out.println(textMsg);
        System.out.println("Received: " + textMsg.getText());
        session.close();
    }

}
