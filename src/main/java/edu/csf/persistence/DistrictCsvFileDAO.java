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

public class DistrictCsvFileDAO extends AbstractCsvFileDAO<Long, District> {
    HouseCsvFileDAO houseCsvFileDAO;
    StoreCsvFileDAO storeCsvFileDAO;
    RelationshipCsvFileDAO<District, Building> relationshipDB;
    AdvertisingCsvFileDAO advertisingCsvFileDAO;
    RelationshipCsvFileDAO<District, Advertising> relationshipDA;
    DateCsvFileDAO dateCsvFileDAO;
    public DistrictCsvFileDAO() throws IOException {
        super("District");
        houseCsvFileDAO = new HouseCsvFileDAO();
        storeCsvFileDAO = new StoreCsvFileDAO();
        relationshipDB = new RelationshipCsvFileDAO<>("DistrictBuilding");
        advertisingCsvFileDAO = new AdvertisingCsvFileDAO();
        relationshipDA = new RelationshipCsvFileDAO<>("DistrictAdvertising");
        dateCsvFileDAO = new DateCsvFileDAO();
    }

    @Override
    public void put(@NotNull District object) {
        var key = object.getIdentity();
        var fields = new Object[]{
        };
        try {
            this.putInCsvInternal(key, fields);
            for (Building building : object.getBuildings()) {
                if (building instanceof House house) {
                    houseCsvFileDAO.put(house);

                } else if (building instanceof Store store) {
                    storeCsvFileDAO.put(store);
                }
                relationshipDB.put(new Relationship<>(object.getIdentity(), building.getIdentity()));
            }
            for (Advertising advertising : object.getActiveAdvertisings()) {
                advertisingCsvFileDAO.put(advertising);
                relationshipDA.put(new Relationship<>(object.getIdentity(), advertising.getIdentity()));
            }
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<District> get(@NotNull Long key) {
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
        var relationship_DB = relationshipDB.getAll(new TwoLong(Long.parseLong(fields[0]), null));
        List<Building> buildings = new ArrayList<>();
        for (Object o : relationship_DB) {
            Optional<Building> building = Optional.empty();
            var house = houseCsvFileDAO.get(((Relationship<?, ?>) o).getIdentity().second);
            if (house.isPresent()) {
                building = Optional.of(house.get());
            } else {
                var store = storeCsvFileDAO.get(((Relationship<?, ?>) o).getIdentity().second);
                if (store.isPresent()) {
                    building = Optional.of(store.get());
                }
            }
            building.ifPresent(buildings::add);
        }
        var relationship_DA = relationshipDA.getAll(new TwoLong(Long.parseLong(fields[0]), null));
        List<Advertising> advertisings = new ArrayList<>();
        for (Object o : relationship_DA) {
            var advertising = advertisingCsvFileDAO.get(((Relationship<?, ?>) o).getIdentity().second);
            advertising.ifPresent(advertisings::add);
        }
        Optional<Date> date = dateCsvFileDAO.get(1L);
        var district = new District(Long.parseLong(fields[0]), buildings, advertisings, date.orElseGet(Date::new));
        return Optional.of(district);
    }
}
