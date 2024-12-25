package org.example.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.OrderHistory;
import org.example.server.mapper.FrontMapper;
import org.example.server.service.FrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class FrontServiceImpl implements FrontService {

    private final FrontMapper frontMapper;

    @Autowired
    public FrontServiceImpl(FrontMapper frontMapper) {
        // フィールドにインターセプターを設定
        this.frontMapper = frontMapper;
    }

    @Override
    public List<OrderHistory> fetchAllCompletedOrders() {
        return frontMapper.fetchAllCompletedOrders();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void changeOrderDishState(OrderHistory orderHistory) {
        frontMapper.changeOrderDishState(orderHistory);
        LocalDateTime earliestOrderTime = frontMapper.fetchEarliestOrderTime(orderHistory);
        if (earliestOrderTime != null) {
            orderHistory.setOrderTime(earliestOrderTime);
            frontMapper.setDeskOrderTime(orderHistory);
        } else {
            frontMapper.setDeskOrderStateToZero(orderHistory);
        }
    }
}
