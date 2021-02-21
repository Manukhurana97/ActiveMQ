package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {
    public String subject;
    public String time;
    public String body;
    public String destination;

    private static final long serialVersionUID = 1L;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
