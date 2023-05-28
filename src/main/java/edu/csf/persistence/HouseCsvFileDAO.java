package edu.csf.persistence;

import edu.csf.domain.*;
import edu.csf.persistence.base.AbstractCsvFileDAO;
import edu.csf.persistence.base.Relationship;
import edu.csf.persistence.base.TwoLong;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HouseCsvFileDAO extends AbstractCsvFileDAO<Long, House> {
    ApartmentCsvFileDAO apartmentCsvFileDAO;
    RelationshipCsvFileDAO<House, Apartment> relationshipHA;
    public HouseCsvFileDAO() throws IOException {
        super("House");
        apartmentCsvFileDAO = new ApartmentCsvFileDAO();
        relationshipHA = new RelationshipCsvFileDAO<>("HouseApartment");
    }

    @Override
    public void put(@NotNull House object) {
        var key = object.getIdentity();
        var fields = new Object[]{
                object.getMonthsBeforeConstruction(),
                object.getNeededMoney(),
                object.getSquareMeterPrice()
        };
        try {
            this.putInCsvInternal(key, fields);
            for (Apartment apartment : object.getApartments()) {
                apartmentCsvFileDAO.put(apartment);
                relationshipHA.put(new Relationship<>(object.getIdentity(), apartment.getIdentity()));
            }
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<House> get(@NotNull Long key) {
        Object[] fieldsInternal;
        try {
            fieldsInternal = this.getFromCsvInternal(key);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        if (fieldsInternal.length == 0) {
            return Optional.empty();
        }
        var fields = ((CSVRecord)fieldsInternal[0]).values();
        var relationship = relationshipHA.getAll(new TwoLong(Long.parseLong(fields[0]), null));
        List<Apartment> apartments = new ArrayList<>();
        for (Object o : relationship) {
            var apartment = apartmentCsvFileDAO.get(((Relationship<?, ?>) o).getIdentity().second);
            apartment.ifPresent(apartments::add);
        }
        var house = new House(Long.parseLong(fields[0]), Integer.parseInt(fields[1]), Double.parseDouble(fields[2]), apartments, Double.parseDouble(fields[3]));
        return Optional.of(house);
    }
}