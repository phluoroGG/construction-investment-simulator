package edu.csf.persistence;

import edu.csf.domain.Store;
import edu.csf.persistence.base.AbstractCsvFileDAO;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class StoreCsvFileDAO extends AbstractCsvFileDAO<Long, Store> {
    public StoreCsvFileDAO() throws IOException {
        super("Store");
    }

    @Override
    public void put(@NotNull Store object) {
        var key = object.getIdentity();
        var fields = new Object[]{
                object.getMonthsBeforeConstruction(),
                object.getNeededMoney()
        };
        try {
            this.putInCsvInternal(key, fields);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Store> get(@NotNull Long key) {
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
        var store = new Store(Long.parseLong(fields[0]), Integer.parseInt(fields[1]), Double.parseDouble(fields[2]));
        return Optional.of(store);
    }
}