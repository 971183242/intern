package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.interfaces.dto.email.MailSenderDTO;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void sendEmailWithTemplate(MailSenderDTO mail) throws MessagingException, IOException, TemplateException;
}
