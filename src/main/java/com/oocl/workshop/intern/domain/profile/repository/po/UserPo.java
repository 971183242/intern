package com.oocl.workshop.intern.domain.profile.repository.po;

import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "T_USER", indexes = @Index(name = "idx_user_team_id", columnList = "team_id"))
@EntityListeners(AuditingEntityListener.class)
public class UserPo {
    @Id
    private String domainId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String role;

    @Embedded
    private InternPeriod internPeriod;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "is_active")
    @ColumnDefault("true")
    private boolean active = true;
}
