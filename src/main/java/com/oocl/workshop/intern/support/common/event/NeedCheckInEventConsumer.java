package com.oocl.workshop.intern.support.common.event;

import com.google.gson.Gson;
import com.oocl.workshop.intern.app.service.EmailService;
import com.oocl.workshop.intern.interfaces.dto.email.MailSenderDTO;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.oocl.workshop.intern.support.ActiveMQConfig.INTERN_NEED_CHECKIN_QUEUE;

@Component
public class NeedCheckInEventConsumer {
    private final Logger logger = LoggerFactory.getLogger(NeedCheckInEventConsumer.class);

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${intern.system.url}")
    private String internSystemUrl;

    @Value("${spring.mail.properties.mail.subject}")
    private String emailSubject;

    @Autowired
    private EmailService emailService;

    @JmsListener(destination = INTERN_NEED_CHECKIN_QUEUE)
    public void receiveMessage(@Payload NeedCheckInEvent event) throws MessagingException, IOException, TemplateException {
        MailSenderDTO mailDto = new MailSenderDTO();
        mailDto.setFrom(emailFrom);
        mailDto.setTo(event.getEmail());
        mailDto.setSubject(emailSubject);
        mailDto.setTemplateName("email-template-checkIn-reminder.ftl");
        Map<String, Object> context = new HashMap<>();
        context.put("internId", event.getName());
        context.put("checkInUrl", internSystemUrl + "intern");
        mailDto.setModel(context);
        emailService.sendEmailWithTemplate(mailDto);
        logger.info(new Gson().toJson(mailDto));
    }
}
