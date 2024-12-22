package org.example.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Order;
import org.example.server.mapper.KitchenMapper;
import org.example.server.service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class KitchenServiceImpl implements KitchenService {
    private final KitchenMapper kitchenMapper;

    @Autowired
    public KitchenServiceImpl(KitchenMapper kitchenMapper) {
        // フィールドにインターセプターを設定
        this.kitchenMapper = kitchenMapper;
    }

    @Override
    public List<Order> fetAllOrders() {
        List<Order> orders = kitchenMapper.fetAllOrders();
        orders.forEach(order -> {
            String orderId = order.getId();
            List<Order.Dishes> dishes = kitchenMapper.fetDishesByOrderId(orderId);
            List<Order.Dishes> orderDishes = order.getDishes();
            orderDishes.addAll(dishes);
        });
        return orders;
    }
}
