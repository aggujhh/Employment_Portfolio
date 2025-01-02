package org.example.server.service;

import org.example.pojo.entity.AccountingOrders;
import org.example.pojo.entity.Operations;
import org.example.pojo.entity.OrderCompletion;
import org.example.pojo.entity.OrderHistory;

import java.util.List;

public interface FrontService {
    List<OrderCompletion> fetchAllCompletedOrders();

    void changeOrderDishState(OrderCompletion orderCompletion);


    AccountingOrders fetchAllAccountingOrders(AccountingOrders accountingOrders);

    void accountingConfirmationCompleted(OrderHistory orderHistory);

    Operations fetchOperations();
}
