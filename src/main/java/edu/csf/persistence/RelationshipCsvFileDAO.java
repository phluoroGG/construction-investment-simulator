package edu.csf.persistence;

import edu.csf.persistence.base.AbstractCsvFileDAO;
import edu.csf.persistence.base.Relationship;
import edu.csf.persistence.base.TwoLong;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class RelationshipCsvFileDAO<T1, T2> extends AbstractCsvFileDAO<TwoLong, Relationship<T1, T2>> {
    public RelationshipCsvFileDAO(String classes) throws IOException {
        super(classes);
    }

    @Override
    public void put(@NotNull Relationship<T1, T2> object) {
        var key = object.getIdentity();
        var fields = new Object[]{
        };
        try {
            this.putInCsvInternal(key, fields);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Relationship<T1, T2>> get(@NotNull TwoLong key) {
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
        var relationship = new Relationship<T1, T2>(Long.parseLong(fields[0]), Long.parseLong(fields[1]));
        return Optional.of(relationship);
    }

    public Object[] getAll(@NotNull TwoLong key) {
        Object[] fieldsInternal;
        try {
            fieldsInternal = this.getFromCsvInternal(key);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        for (int i = 0; i < fieldsInternal.length; i++) {
            var fields = ((CSVRecord)fieldsInternal[i]).values();
            var relationship = new Relationship<T1, T2>(Long.parseLong(fields[0]), Long.parseLong(fields[1]));
            fieldsInternal[i] = relationship;
        }
        return fieldsInternal;
    }
}
