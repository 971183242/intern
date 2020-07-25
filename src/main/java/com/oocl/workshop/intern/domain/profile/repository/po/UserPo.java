package com.oocl.workshop.intern.domain.profile.repository.po;

import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "T_USER", indexes = @Index(name = "idx_user_team_id", columnList = "team_id"))
public class UserPo {
    @Id
    private String domainId;

    private String name;

    private String email;

    private String role;

    @Embedded
    private InternPeriod internPeriod;

    @Column(name = "team_id")
    private String teamId;
}
