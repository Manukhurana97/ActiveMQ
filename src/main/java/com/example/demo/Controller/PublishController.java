package com.example.demo.Controller;

import com.example.demo.model.Email;


import com.fasterxml.jackson.databind.ObjectMapper;
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

//    for passing string
    @PostMapping("/PublishString")
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

//    for passing object
    @PostMapping("/PublishEmail")
    public ResponseEntity<String> publishEmail(@RequestBody Email email){
        try{
            jmsTemplate.send("emailqueue", em -> {
                try{
                    TextMessage tm = em.createTextMessage(new ObjectMapper().writeValueAsString(email));
                    tm.setJMSType(Email.class.getTypeName());
                    tm.setStringProperty("emailinfo", Email.class.getTypeName());
                    System.out.println(tm.getText());
                    return tm;
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            });
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

        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("emailqueue_temp");

        System.out.println("queue: "+queue);
        MessageConsumer consumer = session.createConsumer(queue);
        System.out.println("Start");
        Message receiveObj =  consumer.receive();
        System.out.println("Received: " + receiveObj);
        session.close();
    }

}
