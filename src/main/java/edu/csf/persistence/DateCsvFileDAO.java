package edu.csf.persistence;

import edu.csf.domain.Date;
import edu.csf.persistence.base.AbstractCsvFileDAO;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class DateCsvFileDAO extends AbstractCsvFileDAO<Long, Date> {

    public DateCsvFileDAO() throws IOException {
        super("Date");
    }

    @Override
    public void put(@NotNull Date object) {
        var key = object.getIdentity();
        var fields = new Object[]{
                object.getMonth(),
                object.getPassedMonth()
        };
        try {
            this.putInCsvInternal(key, fields);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Date> get(@NotNull Long key) {
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
        var date = new Date(Long.parseLong(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]));
        return Optional.of(date);
    }
}