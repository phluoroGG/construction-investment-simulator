package edu.csf.domain;

import edu.csf.persistence.base.IdentityInterface;

public abstract class Building implements IdentityInterface<Long> {

    private static Long id = 1L;
    private Long buildingId;
    private int monthsBeforeConstruction;
    private double neededMoney;

    public Building(int monthsBeforeConstruction, double neededMoney) {
        buildingId = id++;
        this.monthsBeforeConstruction = monthsBeforeConstruction;
        this.neededMoney = neededMoney;
    }

    public Building(Long buildingId, int monthsBeforeConstruction, double neededMoney) {
        this.buildingId = buildingId;
        if (buildingId > id) id = buildingId + 1;
        this.monthsBeforeConstruction = monthsBeforeConstruction;
        this.neededMoney = neededMoney;
    }

    @Override
    public Long getIdentity() {
        return this.buildingId;
    }

    @Override
    public void setIdentity(Long identity) {
        this.buildingId = identity;
    }

    public int getMonthsBeforeConstruction() {
        return monthsBeforeConstruction;
    }

    public double getNeededMoney() {
        return neededMoney;
    }

    public double getMonthlySpending() {
        return monthsBeforeConstruction == 0 ? 0 : neededMoney / monthsBeforeConstruction;
    }

    public boolean isConstructed() {
        return monthsBeforeConstruction == 0;
    }

    public void decrementMonths() {
        neededMoney -= neededMoney / monthsBeforeConstruction;
        monthsBeforeConstruction--;
    }
}
