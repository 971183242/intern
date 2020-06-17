package com.oocl.workshop.intern.domain.profile.repository.po;

import com.oocl.workshop.intern.domain.common.BasePo;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_USER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
public abstract class BaseUserPo extends BasePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String domainId;

    private String name;

    private String email;


}
