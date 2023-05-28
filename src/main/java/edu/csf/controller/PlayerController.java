package edu.csf.controller;

import edu.csf.domain.Player;
import edu.csf.persistence.base.DAOInterface;
import edu.csf.reflection.DI;

import java.util.Optional;

public class PlayerController {
    @DI
    private DAOInterface<Long, Player> dao;

    public Optional<Player> get(Long playerId) {
        return dao.get(playerId);
    }

    public void put(Player player) {
        dao.put(player);
    }
}
