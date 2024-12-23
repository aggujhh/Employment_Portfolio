package org.example.server.service;

import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderSnapshot;

import java.util.List;

public interface KitchenService {
    List<Order> fetAllOrders();

    String changeOrderDishState(Order.Dishes dish);

    String resetAllOrderAmdDishState();

    void undoOrRedo(OrderSnapshot version);

    String initializationVersion();
}
