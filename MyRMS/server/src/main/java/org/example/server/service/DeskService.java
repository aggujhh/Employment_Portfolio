package org.example.server.service;

import org.example.pojo.entity.Desk;
import java.util.List;

public interface DeskService {
    List<Desk> fetchAllTables();

    Desk fetchDateByTableId(Desk desk);

    void setCustomerCount(Desk desk);

    void resetAllTables();

    void setDeskOrderStateForThree(String deskId);

    void setDeskOrderStateComeBack(String deskId);

    void disableDeskById(Desk desk);

    void restoreDeskById(Desk desk);
}
