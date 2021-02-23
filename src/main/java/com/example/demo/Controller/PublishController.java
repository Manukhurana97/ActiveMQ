package com.example.demo.Controller;

import com.example.demo.model.Email;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.Enumeration;


@RequestMapping("/Queue")
@RestController
public class PublishController {

    @Autowired
    private JmsTemplate jmsTemplate;



//    for passing string
    @PostMapping("/PublishMessage")
    public ResponseEntity<String> publishMessages(@RequestBody String data){
        try{
            jmsTemplate.convertAndSend("data", data);
            return new ResponseEntity<>("Sent Seccessfully",HttpStatus.OK);
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





//    for passing object
    @PostMapping("/PublishEmail")
    public ResponseEntity<String> publishEmail(@RequestBody Email email){
        try{
            jmsTemplate.send("emailed", em -> {
                try{
                    TextMessage tm = em.createTextMessage(new ObjectMapper().writeValueAsString(email));
                    tm.setJMSType(Email.class.getTypeName());
                    tm.setStringProperty("emailinfo", Email.class.getTypeName());
                    System.out.println(tm);
                    return tm;
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            });
            return new ResponseEntity<>("Sent Seccessfully",HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/ProcessQueue")
    public void processqueue() {
        try {

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session queueSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = queueSession.createQueue("emailed");
            QueueBrowser browser = queueSession.createBrowser(queue);
            Enumeration<?> messagesInQueue = browser.getEnumeration();
            System.out.println(messagesInQueue.toString());

            if ( !messagesInQueue.hasMoreElements() ) {
                System.out.println("No messages in queue");
            } else {
                while (messagesInQueue.hasMoreElements()) {
                    Message tempMsg = (Message)messagesInQueue.nextElement();
                    System.out.println("Message: " + tempMsg); } }
            queueSession.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString() );
        }
    }

}
