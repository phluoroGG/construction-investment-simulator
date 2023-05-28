package edu.csf.controller;

import edu.csf.domain.Date;
import edu.csf.persistence.base.DAOInterface;
import edu.csf.reflection.DI;

import java.util.Optional;

public class DateController {
    @DI
    private DAOInterface<Long, Date> dao;

    public Optional<Date> get(Long dateId) {
        return dao.get(dateId);
    }

    public void put(Date date) {
        dao.put(date);
    }
}
