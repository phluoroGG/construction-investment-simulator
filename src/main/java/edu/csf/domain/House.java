package edu.csf.domain;

import java.util.ArrayList;
import java.util.List;

import static edu.csf.domain.Config.*;


public class House extends Building {
    private List<Apartment> apartments;
    private double squareMeterPrice;


    public House() {
        super(monthHouseBuilding, priceHouse);
        apartments = new ArrayList<>();
        for (int i = 0; i < apartmentCount; i++) {
            apartments.add(new Apartment());
        }
        squareMeterPrice = defaultSquareMeterPrice;
    }

    public House(Long buildingId, int monthsBeforeConstruction, double neededMoney, List<Apartment> apartments, double squareMeterPrice) {
        super(buildingId, monthsBeforeConstruction, neededMoney);
        this.apartments = apartments;
        this.squareMeterPrice = squareMeterPrice;
    }

    public List<Apartment> getApartments() {
        return apartments;
    }

    public double getSquareMeterPrice() {
        return squareMeterPrice;
    }

    public void setSquareMeterPrice(double squareMeterPrice) {
        this.squareMeterPrice = squareMeterPrice;
        for (Apartment apartment : apartments) {
            if (apartment.getState() == ApartmentState.NOFORSALE) {
                apartment.setSquareMeterPrice(squareMeterPrice);
            }
        }
    }

    public int getApartmentCount() {
        return apartments.size();
    }

    public int getNoForSaleCount() {
        int count = 0;
        for (Apartment apartment : apartments) {
            if (apartment.getState() == ApartmentState.NOFORSALE) {
                count++;
            }
        }
        return count;
    }

    public int getForSaleCount() {
        int count = 0;
        for (Apartment apartment : apartments) {
            if (apartment.getState() == ApartmentState.FORSALE) {
                count++;
            }
        }
        return count;
    }

    public int getSoldCount() {
        int count = 0;
        for (Apartment apartment : apartments) {
            if (apartment.getState() == ApartmentState.SOLD) {
                count++;
            }
        }
        return count;
    }
}
