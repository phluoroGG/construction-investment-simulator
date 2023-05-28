package edu.csf.persistence;

import edu.csf.domain.Advertising;
import edu.csf.domain.BuildingType;
import edu.csf.persistence.base.AbstractCsvFileDAO;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class AdvertisingCsvFileDAO extends AbstractCsvFileDAO<Long, Advertising> {
    public AdvertisingCsvFileDAO() throws IOException {
        super("Advertising");
    }

    @Override
    public void put(@NotNull Advertising object) {
        var key = object.getIdentity();
        var fields = new Object[]{
                object.getType().ordinal(),
                object.getPercent(),
                object.getMonthsBeforeExpire()
        };
        try {
            this.putInCsvInternal(key, fields);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Advertising> get(@NotNull Long key) {
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
        var advertising = new Advertising(Long.parseLong(fields[0]), BuildingType.values()[(Integer.parseInt(fields[1]))], Double.parseDouble(fields[2]), Integer.parseInt(fields[3]));
        return Optional.of(advertising);
    }
}
