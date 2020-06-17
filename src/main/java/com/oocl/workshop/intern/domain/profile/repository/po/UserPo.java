package com.oocl.workshop.intern.domain.profile.repository.po;

import com.oocl.workshop.intern.domain.common.BasePo;
import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_USER")
public class UserPo extends BasePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String domainId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Embedded
    private InternPeriod internPeriod;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "team_id")
    private TeamPo team;
}
