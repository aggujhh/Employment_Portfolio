package org.example.server.service;
import org.example.pojo.entity.Dish;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderHistory;

import java.util.List;

public interface OrderService {
    void addOrder(Order order);

    Integer getOrderTotalPrice(String order);

    List<Order.Dishes> fetchAllCompletedOrders(String deskId);

    void finishOrder(OrderHistory order);

    List<Dish> getDishByCategoryId(Long dishCategoryId);
}
