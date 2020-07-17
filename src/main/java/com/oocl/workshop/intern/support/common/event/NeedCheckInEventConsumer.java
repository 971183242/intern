package com.oocl.workshop.intern.support.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.oocl.workshop.intern.support.ActiveMQConfig.INTERN_NEED_CHECKIN_QUEUE;

@Component
public class NeedCheckInEventConsumer {
    private final Logger logger = LoggerFactory.getLogger(NeedCheckInEventConsumer.class);

    @JmsListener(destination = INTERN_NEED_CHECKIN_QUEUE)
    public void receiveMessage(@Payload NeedCheckInEvent event) {
        logger.debug(event.toString());
    }
}
