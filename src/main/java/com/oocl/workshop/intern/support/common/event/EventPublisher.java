package com.oocl.workshop.intern.support.common.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import static com.oocl.workshop.intern.support.ActiveMQConfig.INTERN_QUEUE;
import static com.oocl.workshop.intern.support.ActiveMQConfig.REPORT_QUEUE;

@Service
public class EventPublisher {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(DomainEvent event) {
        jmsTemplate.convertAndSend(INTERN_QUEUE, event);
    }

    public void triggerReportEvent(DomainEvent event) {
        jmsTemplate.convertAndSend(REPORT_QUEUE, event);
    }
}