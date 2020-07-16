package com.oocl.workshop.intern.support.common.event;

import lombok.Data;

@Data
public class NeedCheckInEvent extends DomainEvent {
    private String domainId;
    private String email;
    private String name;
}
