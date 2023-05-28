package edu.csf.domain;

import edu.csf.persistence.base.IdentityInterface;

import java.util.ArrayList;
import java.util.List;

import static edu.csf.domain.Config.*;

public class District implements IdentityInterface<Long> {

    private static Long id = 1L;
    private Long districtId;
    private List<Building> buildings;
    private List<Advertising> activeAdvertisings;
    private Date date;

    public District(Date date) {
        districtId = id++;
        buildings = new ArrayList<>();
        activeAdvertisings = new ArrayList<>();
        this.date = date;
    }

    public District(Long districtId, List<Building> buildings, List<Advertising> activeAdvertisings, Date date) {
        this.districtId = districtId;
        if (districtId > id) id = districtId + 1;
        this.buildings = buildings;
        this.activeAdvertisings = activeAdvertisings;
        this.date = date;
    }

    @Override
    public Long getIdentity() {
        return this.districtId;
    }

    @Override
    public void setIdentity(Long identity) {
        this.districtId = identity;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public List<Advertising> getActiveAdvertisings() {
        return activeAdvertisings;
    }

    public void addHouse() { // do smth with this
        buildings.add(new House());
    }

    public void addStore() { // do smth with this
        buildings.add(new Store());
    }

    public int getHouseCount() {
        int count = 0;
        for (Building building : buildings) {
            if (building instanceof House && building.isConstructed())
                count++;
        }
        return count;
    }

    public int getStoreCount() {
        int count = 0;
        for (Building building : buildings) {
            if (building instanceof Store && building.isConstructed())
                count++;
        }
        return count;
    }

    public int getUnbuiltHouseCount() {
        int count = 0;
        for (Building building : buildings) {
            if (building instanceof House && !building.isConstructed())
                count++;
        }
        return count;
    }

    public int getUnbuiltStoreCount() {
        int count = 0;
        for (Building building : buildings) {
            if (building instanceof Store && !building.isConstructed())
                count++;
        }
        return count;
    }

    public double getEstimateSpending() {
        double spending = 0;
        for (Building building : buildings) {
            spending += building.getNeededMoney();
        }
        return spending;
    }

    public double getEstimateSpendingPerMonth() {
        double spending = 0;
        for (Building building : buildings) {
            spending += building.getMonthlySpending();
        }
        return spending;
    }

    public double getMonthSales() {
        return moneyFromSales * getStoreCount() * getPercentSales();
    }

    public double getPercentDemand() {

        double demandFactor = 1;
        demandFactor *= date.getDemandFactor();
        demandFactor *= 1 + increaseDemand * getStoreCount() / 100;
        double sum = 0;
        for (Advertising advertising : activeAdvertisings) {
            if (advertising.getType() == BuildingType.HOUSE) {
                sum += advertising.getPercent();
            }
        }
        demandFactor *= 1 + sum / 100;
        return demandFactor;
    }

    public double getPercentSales() {
        double salesFactor = 1;
        salesFactor *= date.getSalesFactor();
        salesFactor *= 1 + increaseSales * getHouseCount() / 100;
        double sum = 0;
        for (Advertising advertising : activeAdvertisings) {
            if (advertising.getType() == BuildingType.STORE) {
                sum += advertising.getPercent();
            }
        }
        salesFactor *= 1 + sum / 100;
        return salesFactor;
    }

    public int getNoForSaleCount() {
        int count = 0;
        for (Building building : buildings) {
            if (building instanceof House house)
                count += house.getNoForSaleCount();
        }
        return count;
    }

    public int getForSaleCount() {
        int count = 0;
        for (Building building : buildings) {
            if (building instanceof House house)
                count += house.getForSaleCount();
        }
        return count;
    }

    public int getSoldCount() {
        int count = 0;
        for (Building building : buildings) {
            if (building instanceof House house)
                count += house.getSoldCount();
        }
        return count;
    }

    public void putUpForSale(int apartmentCount, int monthLimit) {
        for (Building building : buildings) {
            if (building instanceof House house) {
                if (building.getMonthsBeforeConstruction() > monthLimit) {
                    continue;
                }
                List<Apartment> apartments = house.getApartments();
                int count = house.getNoForSaleCount();
                if (count != 0) {
                    for (Apartment apartment : apartments) {
                        if (apartmentCount == 0) {
                            break;
                        }
                        apartment.setState(ApartmentState.FORSALE);
                        apartmentCount--;
                    }
                }
            }
            if (apartmentCount == 0) {
                break;
            }
        }
    }

    public double sellApartments(int apartmentCount) {
        double money = 0;
        for (Building building : buildings) {
            if (building instanceof House house) {
                List<Apartment> apartments = house.getApartments();
                int count = house.getForSaleCount();
                if (count != 0) {
                    for (Apartment apartment : apartments) {
                        if (apartmentCount == 0) {
                            break;
                        }
                        if (apartment.getState() == ApartmentState.FORSALE) {
                            money += apartment.sell();
                            apartmentCount--;
                        }
                    }
                }
            }
            if (apartmentCount == 0) {
                break;
            }
        }
        return money;
    }

    public void setSquareMeterPrice(double squareMeterPrice) {
        for (Building building : buildings) {
            if (building instanceof House house) {
                house.setSquareMeterPrice(squareMeterPrice);
            }
        }
    }

    public void buyDemandAdvertising(double money) {
        activeAdvertisings.add(new Advertising(BuildingType.HOUSE, money));
    }

    public void buySalesAdvertising(double money) {
        activeAdvertisings.add(new Advertising(BuildingType.STORE, money));
    }

    public void iterate() {
        for (Building building : buildings)
            if (!building.isConstructed()) {
                building.decrementMonths();
            }
        for (int i = 0; i < activeAdvertisings.size(); i++) {
            activeAdvertisings.get(i).decrementMonths();
            if (activeAdvertisings.get(i).isExpired()) {
                activeAdvertisings.remove(i);
                i--;
            }
        }
    }
}
