package com.oocl.workshop.intern.domain.attendance.repostitory.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "T_SETTLEMENT_DAY")
public class MonthlySettlementDayRule {
    public static final int DEFAULT_DAY = 25;

    @Id
    private int day = DEFAULT_DAY;

}
