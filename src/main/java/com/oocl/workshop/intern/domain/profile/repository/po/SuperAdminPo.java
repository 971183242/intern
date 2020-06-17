package com.oocl.workshop.intern.domain.profile.repository.po;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SuperAdmin")
public class SuperAdminPo extends BaseUserPo {
}
