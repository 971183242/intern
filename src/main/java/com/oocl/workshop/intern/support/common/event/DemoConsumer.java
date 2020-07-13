package com.oocl.workshop.intern.support.common.event;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import javax.jms.Session;

import static com.oocl.workshop.intern.support.ActiveMQConfig.INTERN_QUEUE;

public class DemoConsumer {
    @JmsListener(destination = INTERN_QUEUE)
    public void receiveMessage(@Payload DomainEvent event,
                               @Headers MessageHeaders headers,
                               Message message, Session session) {
        System.out.println("received <" + event + ">");

        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("######          Message Details           #####");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - -");
        System.out.println("headers: " + headers);
        System.out.println("message: " + message);
        System.out.println("session: " + session);
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - -");
    }
}
