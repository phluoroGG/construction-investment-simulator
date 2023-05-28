package edu.csf.persistence;

import edu.csf.domain.Agency;
import edu.csf.domain.Player;
import edu.csf.persistence.base.AbstractCsvFileDAO;
import edu.csf.persistence.base.Relationship;
import edu.csf.persistence.base.TwoLong;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgencyCsvFileDAO extends AbstractCsvFileDAO<Long, Agency> {
    PlayerCsvFileDAO playerCsvFileDAO;
    RelationshipCsvFileDAO<Agency, Player> relationshipAP;
    public AgencyCsvFileDAO() throws IOException {
        super("Agency");
        playerCsvFileDAO = new PlayerCsvFileDAO();
        relationshipAP = new RelationshipCsvFileDAO<>("AgencyPlayer");
    }

    @Override
    public void put(@NotNull Agency object) {
        var key = object.getIdentity();
        var fields = new Object[]{
        };
        try {
            this.putInCsvInternal(key, fields);
            for (Player player : object.getPlayers()) {
                relationshipAP.put(new Relationship<>(object.getIdentity(), player.getIdentity()));
            }

        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Agency> get(@NotNull Long key) {
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
        var relationship = relationshipAP.getAll(new TwoLong(Long.parseLong(fields[0]), null));
        List<Player> players = new ArrayList<>();
        for (Object o : relationship) {
            var player = playerCsvFileDAO.get(((Relationship<?, ?>) o).getIdentity().second);
            player.ifPresent(players::add);
        }
        var agency = new Agency(Long.parseLong(fields[0]), players);
        return Optional.of(agency);
    }
}
