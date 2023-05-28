package edu.csf.persistence;

import edu.csf.domain.Date;
import edu.csf.domain.District;
import edu.csf.domain.Player;
import edu.csf.persistence.base.AbstractCsvFileDAO;
import edu.csf.persistence.base.Relationship;
import edu.csf.persistence.base.TwoLong;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Optional;

public class PlayerCsvFileDAO extends AbstractCsvFileDAO<Long, Player> {
    DistrictCsvFileDAO districtCsvFileDAO;
    RelationshipCsvFileDAO<Player, District> relationshipPD;
    public PlayerCsvFileDAO() throws IOException {
        super("Player");
        districtCsvFileDAO = new DistrictCsvFileDAO();
        relationshipPD = new RelationshipCsvFileDAO<>("PlayerDistrict");
    }

    @Override
    public void put(@NotNull Player object) {
        var key = object.getIdentity();
        var fields = new Object[]{
                object.getMoney(),
                object.getCapital()
        };
        try {
            this.putInCsvInternal(key, fields);
            districtCsvFileDAO.put(object.getDistrict());
            relationshipPD.put(new Relationship<>(object.getIdentity(), object.getDistrict().getIdentity()));
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Player> get(@NotNull Long key) {
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
        var relationship = relationshipPD.get(new TwoLong(Long.parseLong(fields[0]), null));
        Optional<District> district = Optional.empty();
        if (relationship.isPresent()) {
            district = districtCsvFileDAO.get(relationship.get().getIdentity().second);
        }
        if (district.isEmpty()) {
            district = Optional.of(new District(new Date()));
        }
        var player = new Player(Long.parseLong(fields[0]), district.get(), Double.parseDouble(fields[1]), Double.parseDouble(fields[2]));
        return Optional.of(player);
    }
}
