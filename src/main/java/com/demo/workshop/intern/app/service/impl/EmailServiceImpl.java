package com.demo.workshop.intern.app.service.impl;

import com.demo.workshop.intern.app.service.EmailService;
import com.demo.workshop.intern.interfaces.dto.email.MailSenderDTO;
import com.demo.workshop.intern.support.util.EmailUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration freemarkerConfig;

    private String templateLoaderPath = "/templates";

    @Override
    public void sendEmailWithTemplate(MailSenderDTO mail) throws MessagingException, IOException, TemplateException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());


        Template t = getFreemarkerConfig().getTemplate(mail.getTemplateName());
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

        helper.setFrom(mail.getFrom());
        helper.setTo(EmailUtil.parseEmailAddresses(mail.getTo()));
        if (StringUtils.isNotEmpty(mail.getCc())) {
            helper.setCc(EmailUtil.parseEmailAddresses(mail.getCc()));
        }
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        if (mail.getAttachments() != null) {
            for (Object o : mail.getAttachments()) {
                if (o instanceof File) {
                    File file = (File) o;
                    helper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
                }
            }
        }
        emailSender.send(message);
    }

    private Configuration getFreemarkerConfig() throws IOException, TemplateException {
        if (freemarkerConfig == null) {
            FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
            freemarkerConfig = factory.createConfiguration();
            freemarkerConfig.setDirectoryForTemplateLoading(new File(templateLoaderPath));
        }
        return freemarkerConfig;
    }
}
