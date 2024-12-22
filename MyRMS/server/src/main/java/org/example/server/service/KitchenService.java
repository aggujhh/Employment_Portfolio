package org.example.server.service;

import org.example.pojo.entity.Order;

import java.util.List;

public interface KitchenService {
    List<Order> fetAllOrders();
}
