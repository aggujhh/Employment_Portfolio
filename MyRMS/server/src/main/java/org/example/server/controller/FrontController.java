package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.common.SseService;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderHistory;
import org.example.server.service.FrontService;
import org.example.server.service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/front")
public class FrontController {

    private final FrontService frontService;


    @Autowired
    public FrontController(FrontService frontService) {
        // フィールドにインターセプターを設定
        this.frontService = frontService;
    }

    @GetMapping
    public Result fetchAllCompletedOrders() {
        log.info("調理済オーダーをすべてフェッチ。");
        List<OrderHistory> results = frontService.fetchAllCompletedOrders();
        log.info("フェッチ成功:{}", results);
        return Result.success(results);
    }


}
