package com.oocl.workshop.intern.support.common.event;

import com.google.gson.Gson;
import com.oocl.workshop.intern.app.service.EmailService;
import com.oocl.workshop.intern.interfaces.dto.email.MailSenderDTO;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EmailService emailService;

    @JmsListener(destination = INTERN_NEED_CHECKIN_QUEUE)
    public void receiveMessage(@Payload NeedCheckInEvent event) throws MessagingException, IOException, TemplateException {
        MailSenderDTO mailDto = new MailSenderDTO();
        mailDto.setFrom("grace.w.j.chen@oocl.com");
        mailDto.setTo("grace.w.j.chen@oocl.com");
        mailDto.setSubject("实习生管理系统-签到提醒");
        mailDto.setCc("grace.w.j.chen@oocl.com");
        mailDto.setTemplateName("email-template-checkIn-reminder.ftl");
        Map<String, Object> context = new HashMap<>();
        context.put("internId", event.getName());
        mailDto.setModel(context);
        emailService.sendEmailWithTemplate(mailDto);
        logger.info(new Gson().toJson(mailDto));
    }
}
