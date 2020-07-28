package com.demo.workshop.intern.support.common.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(DomainEvent event, String queue) {
        jmsTemplate.convertAndSend(queue, event);
    }
}