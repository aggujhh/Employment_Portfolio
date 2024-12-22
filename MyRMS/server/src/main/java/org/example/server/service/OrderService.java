package org.example.server.service;
import org.example.pojo.entity.Order;

import java.util.List;

public interface OrderService {
    void addOrder(Order order);

    Integer getOrderTotalPrice(String order);

    List<Order.Dishes> fetchAllCompletedOrders(String deskId);
}
