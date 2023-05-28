package edu.csf.domain;

import edu.csf.persistence.base.IdentityInterface;

import static edu.csf.domain.Config.*;

public class Apartment implements IdentityInterface<Long> {

    private static Long id = 1L;
    private Long apartmentId;
    private double square;
    private ApartmentState state;
    private double squareMeterPrice;



    public Apartment() {
        apartmentId = id++;
        square = defaultSquare;
        state = ApartmentState.NOFORSALE;
        squareMeterPrice = defaultSquareMeterPrice;
    }

    public Apartment(Long apartmentId, double square, ApartmentState state, double squareMeterPrice) {
        this.apartmentId = apartmentId;
        if (apartmentId > id) id = apartmentId + 1;
        this.square = square;
        this.state = state;
        this.squareMeterPrice = squareMeterPrice;
    }

    @Override
    public Long getIdentity() {
        return this.apartmentId;
    }

    @Override
    public void setIdentity(Long identity) {
        this.apartmentId = identity;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public double getSquareMeterPrice() {
        return squareMeterPrice;
    }

    public void setSquareMeterPrice(double squareMeterPrice) {
        this.squareMeterPrice = squareMeterPrice;
    }

    public ApartmentState getState() {
        return state;
    }

    public void setState(ApartmentState state) {
        this.state = state;
    }

    public double sell() {
        state = ApartmentState.SOLD;
        return squareMeterPrice * square;
    }
}
