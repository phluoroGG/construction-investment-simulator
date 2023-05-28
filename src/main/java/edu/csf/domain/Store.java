package edu.csf.domain;
import static edu.csf.domain.Config.*;

public class Store extends Building {

    public Store() {
        super(monthStoreBuilding, priceStore);
    }

    public Store(Long buildingId, int monthsBeforeConstruction, double neededMoney) {
        super(buildingId, monthsBeforeConstruction, neededMoney);
    }
}
