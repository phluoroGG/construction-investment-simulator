package edu.csf.domain;
import edu.csf.persistence.base.IdentityInterface;

import static edu.csf.domain.Config.*;

import java.util.ArrayList;
import java.util.List;

public class Agency implements IdentityInterface<Long> {

    private static Long id = 1L;
    private Long agencyId;
    private List<Player> players;

    public Agency() {
        agencyId = id++;
        players = new ArrayList<>();
    }

    public Agency(Long playerId, List<Player> players) {
        this.agencyId = playerId;
        if (playerId > id) id = playerId + 1;
        this.players = players;
    }

    @Override
    public Long getIdentity() {
        return this.agencyId;
    }

    @Override
    public void setIdentity(Long identity) {
        this.agencyId = identity;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void iterate() {
        for (Player player : players) {
            int demand = (int) (defaultDemand * player.getDistrict().getPercentDemand());
            int forSaleCount = player.getDistrict().getForSaleCount();
            if (demand > forSaleCount) {
                demand = forSaleCount;
            }
            player.getMoneyFromAgency(player.getDistrict().sellApartments(demand));
        }
    }
}
