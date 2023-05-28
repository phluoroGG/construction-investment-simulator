package edu.csf.domain;

import edu.csf.persistence.base.IdentityInterface;

import static edu.csf.domain.Config.*;

public class Date implements IdentityInterface<Long> {

    private static Long id = 1L;
    private Long dateId;
    private int month;
    private int passedMonth;



    public Date() {
        dateId = id++;
        month = startDate;
        passedMonth = 0;
    }

    public Date(Long dateId, int month, int passedMonth) {
        this.dateId = dateId;
        if (dateId > id) id = dateId + 1;
        this.month = month;
        this.passedMonth = passedMonth;
    }

    @Override
    public Long getIdentity() {
        return this.dateId;
    }

    @Override
    public void setIdentity(Long identity) {
        this.dateId = identity;
    }

    public int getMonth() {
        return month;
    }

    public int getPassedMonth() {
        return passedMonth;
    }

    public void incrementMonth() {
        if (month == 12) {
            month = 1;
        }
        else {
            month++;
        }
        passedMonth++;
    }

    public double getDemandFactor() {
        return demandFactor[month - 1];
    }

    public double getSalesFactor() {
        return salesFactor[month - 1];
    }

    public boolean isEnd() {
        return passedMonth >= monthLimit;
    }
}
