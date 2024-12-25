package org.example.server.service;

import org.example.pojo.entity.OrderHistory;

import java.util.List;

public interface FrontService {
    List<OrderHistory> fetchAllCompletedOrders();

    void changeOrderDishState(OrderHistory orderHistory);
}
