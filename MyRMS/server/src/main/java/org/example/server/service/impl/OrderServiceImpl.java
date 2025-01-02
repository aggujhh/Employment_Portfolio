package org.example.server.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.OrderHistory;
import org.example.server.mapper.OrderMapper;
import org.example.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.pojo.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addOrder(Order order) {
        String orderId = NanoIdUtils.randomNanoId();
        order.setId(orderId);
        order.setOrderTime(LocalDateTime.now());
        orderMapper.addOrder(order);
        order.getDishes().forEach(dish -> {
            dish.setOrderId(orderId);
            orderMapper.addOrderDishRelation(dish);
        });
        String deskId = order.getDeskId();
        boolean hasOrderTime = orderMapper.checkDeskHasOrderTime(deskId);
        if (hasOrderTime) {
            orderMapper.setNewDeskState(deskId);
        } else {
            orderMapper.setNewDeskStateAndOrderTime(deskId);
        }
    }

    @Override
    public Integer getOrderTotalPrice(String deskId) {
        List<String> orderIds = orderMapper.fetchOrderByDeskId(deskId);
        AtomicInteger totalPrice = new AtomicInteger(0);
        orderIds.forEach(orderId -> {
            List<Map<String, Object>> deskPriceAndCount = orderMapper.fetchDeskPriceAndCountByOrderId(orderId);
            deskPriceAndCount.forEach(map -> {
                // 从 Map 中提取值
                Integer price = (Integer) map.get("price");
                Integer count = (Integer) map.get("count");
                // 累加总价
                totalPrice.addAndGet(price * count);
            });
        });
        return totalPrice.get();
    }

    @Override
    public List<Order.Dishes> fetchAllCompletedOrders(String deskId) {
        // 指定されたデスクIDに関連するすべての注文IDを取得
        List<String> orderIds = orderMapper.fetchOrderByDeskId(deskId);
        // dishId と dishState をキーにして、結果を保存するためのマップ
        Map<String, Order.Dishes> dishMap = new HashMap<>();

        // すべての注文IDをループして処理
        orderIds.forEach(orderId -> {
            // 現在の注文IDに関連するすべての注文済み料理を取得
            List<Order.Dishes> results = orderMapper.fetchAllCompletedOrders(orderId);
            for (Order.Dishes result : results) {
                Integer dishId = result.getDishId(); // 料理IDを取得
                String dishState = result.getState(); // 料理の状態を取得

                // ユニークキーを作成 (dishId + "-" + dishState)
                String key = dishId + "-" + dishState;

                if (dishMap.containsKey(key)) {
                    // 同じキーが存在する場合、count（数量）を合計
                    Order.Dishes existingDish = dishMap.get(key);
                    existingDish.setCount(existingDish.getCount() + result.getCount());
                } else {
                    // 存在しない場合、新しい料理をマップに追加
                    dishMap.put(key, result);
                }
            }
        });

        // マップの値をリストに変換し、dishId と dishState を分離
        List<Order.Dishes> dishes = new ArrayList<>();
        dishMap.forEach((key, value) -> {
            // キーを分割して dishId と dishState を取得
            String[] parts = key.split("-");
            Integer dishId = Integer.valueOf(parts[0]); // dishId
            String dishState = parts[1]; // dishState

            // Order.Dishes オブジェクトに更新
            value.setDishId(dishId);
            value.setState(dishState);

            // リストに追加
            dishes.add(value);
        });

        // 処理結果のリストを返す
        return dishes;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void finishOrder(OrderHistory orderHistory) {
        String deskId = orderHistory.getDeskId();
        List<String> orderIds = orderMapper.fetchOrderByDeskId(deskId);
        orderIds.forEach(orderMapper::setOrderDishStateToThreeByOrderId);
        orderMapper.setOrderStateToThree(deskId);
        orderMapper.setDeskOrderStateToTwo(orderHistory);
    }
}
