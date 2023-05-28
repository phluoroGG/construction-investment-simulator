package edu.csf.controller;

import edu.csf.domain.Agency;
import edu.csf.persistence.base.DAOInterface;
import edu.csf.reflection.DI;

import java.util.Optional;

public class AgencyController {
    @DI
    private DAOInterface<Long, Agency> dao;

    public Optional<Agency> get(Long agencyId) {
        return dao.get(agencyId);
    }

    public void put(Agency agency) {
        dao.put(agency);
    }
}
