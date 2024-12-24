package org.example.server.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderHistory;
import org.example.server.mapper.EmpMapper;
import org.example.server.mapper.FrontMapper;
import org.example.server.service.FrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
