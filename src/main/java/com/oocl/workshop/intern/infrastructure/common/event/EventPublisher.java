package com.oocl.workshop.intern.infrastructure.common.event;

import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    public void publish(DomainEvent event) {
        //send to MQ
        //mq.send(event);
    }
}