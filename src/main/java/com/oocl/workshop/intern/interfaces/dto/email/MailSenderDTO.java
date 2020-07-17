package com.oocl.workshop.intern.interfaces.dto.email;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MailSenderDTO {
    private String from;
    private String to;
    private String cc;
    private String subject;
    private String templateName;
    private List<Object> attachments;
    private Map<String, Object> model;
}
