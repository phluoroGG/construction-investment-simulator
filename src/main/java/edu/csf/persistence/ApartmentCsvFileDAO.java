package edu.csf.persistence;

import edu.csf.domain.Apartment;
import edu.csf.domain.ApartmentState;
import edu.csf.persistence.base.AbstractCsvFileDAO;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class ApartmentCsvFileDAO extends AbstractCsvFileDAO<Long, Apartment> {
    public ApartmentCsvFileDAO() throws IOException {
        super("Apartment");
    }

    @Override
    public void put(@NotNull Apartment object) {
        var key = object.getIdentity();
        var fields = new Object[]{
                object.getSquare(),
                object.getState().ordinal(),
                object.getSquareMeterPrice()
        };
        try {
            this.putInCsvInternal(key, fields);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Apartment> get(@NotNull Long key) {
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
        var apartment = new Apartment(Long.parseLong(fields[0]), Double.parseDouble(fields[1]), ApartmentState.values()[Integer.parseInt(fields[2])], Double.parseDouble(fields[3]));
        return Optional.of(apartment);
    }
}