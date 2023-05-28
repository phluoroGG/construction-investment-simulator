package edu.csf.domain;

import edu.csf.persistence.base.IdentityInterface;

import static edu.csf.domain.Config.*;

public class Player implements IdentityInterface<Long> {

    private static Long id = 1L;
    private Long playerId;
    private District district;
    private double money;
    private double capital;

    public Player(Date date) {
        playerId = id++;
        district = new District(date);
        money = startingMoney;
    }

    public Player(Long playerId, District district, double money, double capital) {
        this.playerId = playerId;
        if (playerId > id) id = playerId + 1;
        this.district = district;
        this.money = money;
        this.capital = capital;
    }

    @Override
    public Long getIdentity() {
        return this.playerId;
    }

    @Override
    public void setIdentity(Long identity) {
        this.playerId = identity;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public void buildHouse() {
        district.addHouse();
    }

    public void buildStore() {
        district.addStore();
    }

    public int getHouseCount() {
        return district.getHouseCount();
    }

    public int getStoreCount() {
        return district.getStoreCount();
    }

    public int getUnbuiltHouseCount() {
        return district.getUnbuiltHouseCount();
    }

    public int getUnbuiltStoreCount() {
        return district.getUnbuiltStoreCount();
    }

    public double getEstimateSpending() {
        return district.getEstimateSpending();
    }

    public double getEstimateSpendingPerMonth() {
        return district.getEstimateSpendingPerMonth();
    }

    public void putUpForSale(int apartmentCount, int monthLimit) {
        district.putUpForSale(apartmentCount, monthLimit);
    }

    public void setSquareMeterPrice(double squareMeterPrice) {
        district.setSquareMeterPrice(squareMeterPrice);
    }

    public double getMonthSales() {
        return district.getMonthSales();
    }

    public double getPercentDemand() {
        return district.getPercentDemand();
    }

    public double getPercentSales() {
        return district.getPercentSales();
    }

    public int getNoForSaleCount() {
        return district.getNoForSaleCount();
    }

    public int getForSaleCount() {
        return district.getForSaleCount();
    }

    public int getSoldCount() {
        return district.getSoldCount();
    }

    public void buyDemandAdvertising(double money) {
        district.buyDemandAdvertising(money);
    }

    public void buySalesAdvertising(double money) {
        district.buySalesAdvertising(money);
    }

    public void getMoneyFromAgency(double money) {
        this.money += money;
    }

    public void iterate() {
        double estimateSpending = getEstimateSpendingPerMonth();
        money -= estimateSpending;
        capital += estimateSpending;
        double sales = district.getMonthSales();
        money += sales;
        capital += sales;
        int houseCount = district.getUnbuiltHouseCount();
        int storeCount = district.getUnbuiltStoreCount();
        district.iterate();
        houseCount -= district.getUnbuiltHouseCount();
        storeCount -= district.getUnbuiltStoreCount();
        capital += houseCount * (defaultSquareMeterPrice * defaultSquare * apartmentCount - priceHouse);
        capital += storeCount * priceStore * 0.6;
    }
}
