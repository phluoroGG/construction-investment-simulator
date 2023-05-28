package edu.csf.domain;

import edu.csf.persistence.base.IdentityInterface;

import static edu.csf.domain.Config.*;

public class Advertising implements IdentityInterface<Long> {
    private static Long id = 1L;
    private Long advertisingId;
    private final BuildingType type;
    private double percent;
    private int monthsBeforeExpire;



    public Advertising(BuildingType type, double investedMoney) {
        advertisingId = id++;
        monthsBeforeExpire = 2;
        this.type = type;
        switch (type) {
            case HOUSE:
                percent = investedMoney / 1000 * increasingHouse;
                break;
            case STORE:
                percent = investedMoney / 1000 * increasingStore;
                break;
        }
    }

    public Advertising(Long advertisingId, BuildingType type, double percent, int monthsBeforeExpire) {
        this.advertisingId = advertisingId;
        if (advertisingId > id) id = advertisingId + 1;
        this.type = type;
        this.percent = percent;
        this.monthsBeforeExpire = monthsBeforeExpire;
    }

    @Override
    public Long getIdentity() {
        return this.advertisingId;
    }

    @Override
    public void setIdentity(Long identity) {
        this.advertisingId = identity;
    }

    public BuildingType getType() {
        return type;
    }

    public double getPercent() {
        return percent;
    }

    public int getMonthsBeforeExpire() {
        return monthsBeforeExpire;
    }

    public void decrementMonths() {
        monthsBeforeExpire--;
    }

    public boolean isExpired() {
        return monthsBeforeExpire == 0;
    }


}
