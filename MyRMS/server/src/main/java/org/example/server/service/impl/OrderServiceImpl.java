package org.example.server.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.example.server.mapper.OrderMapper;
import org.example.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.pojo.entity.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public void addOrder(Order order) {
        order.setId(NanoIdUtils.randomNanoId());
        order.setOrderTime(LocalDateTime.now());
        orderMapper.addOrder(order);
        order.getDishes().forEach(dish -> {

        });
    }
}
