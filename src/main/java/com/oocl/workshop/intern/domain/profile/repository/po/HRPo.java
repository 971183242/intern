package com.oocl.workshop.intern.domain.profile.repository.po;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("HR")
public class HRPo extends BaseUserPo {
}
