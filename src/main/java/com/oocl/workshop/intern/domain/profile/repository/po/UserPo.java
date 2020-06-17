package com.oocl.workshop.intern.domain.profile.repository.po;

import com.oocl.workshop.intern.domain.common.BasePo;
import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_USER")
@EntityListeners(AuditingEntityListener.class)
public class UserPo extends BasePo {
    @Id
    private String domainId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Embedded
    private InternPeriod internPeriod;

    private String teamId;
}
