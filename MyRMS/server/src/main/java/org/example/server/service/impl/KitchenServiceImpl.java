package org.example.server.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderHistory;
import org.example.pojo.entity.OrderSnapshot;
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

    private String getDeskIdByOrderId(String id) {
        return kitchenMapper.getDeskIdByOrderId(id);
    }

    @Override
    public String changeOrderDishState(Order.Dishes dish) {
        kitchenMapper.changeOrderDishState(dish);
        Integer count = kitchenMapper.countCookedDish(dish);
        String id = dish.getOrderId();
        if (count == 0) {
            kitchenMapper.changeOrderState(id);
        }
        String deskId = getDeskIdByOrderId(id);
        kitchenMapper.setTableOrderState(deskId);
        return initializationVersion();
    }

    @Override
    public String changeOrderState(Order order) {
        String id = order.getId();
        kitchenMapper.changeOrderState(id);
        kitchenMapper.changeAllDishStateByOrderId(id);
        String deskId = getDeskIdByOrderId(id);
        kitchenMapper.setTableOrderState(deskId);
        return initializationVersion();
    }

    @Override
    public List<OrderHistory> fetchAllOrderHistory() {
        return kitchenMapper.fetchAllOrderHistory();
    }

    @Override
    public String resetAllOrderAmdDishState() {
        kitchenMapper.resetAllOrderState();
        kitchenMapper.resetAllOrderDishState();
        kitchenMapper.resetAllDeskState();
        return initializationVersion();
    }

    @Override
    public void undoOrRedo(OrderSnapshot orderSnapshot) {
        // 一次性获取所有订单快照
        List<OrderSnapshot> orders = kitchenMapper.fetchOrderIdAndOrderStateByVersion(orderSnapshot);
        // 一次性获取所有菜品快照
        List<OrderSnapshot> dishes = kitchenMapper.fetchDishIdAndDishStateByVersion(orderSnapshot);

        // 遍历订单快照并处理
        orders.forEach(order -> {
            // 撤销或重做订单
            kitchenMapper.undoOrRedoOrder(order);
            // 过滤出属于当前订单的菜品快照
            List<OrderSnapshot> filteredDishes = dishes.stream()
                    .filter(dish -> dish.getOrderId().equals(order.getOrderId()))
                    .toList();
            // 遍历并处理菜品
            filteredDishes.forEach(dish -> kitchenMapper.undoOrRedoDish(order.getOrderId(), dish));
        });
    }

    @Override
    public String initializationVersion() {
        String version = NanoIdUtils.randomNanoId();
        kitchenMapper.saveOrderSnapshot(version);
        Integer versionCount = kitchenMapper.countVersion();
        if (versionCount > 10) {
            kitchenMapper.deleteFirstSnapshot();
        }
        return version;
    }


}
