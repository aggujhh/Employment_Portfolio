package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.DishCategory;
import org.example.pojo.entity.Order;
import org.example.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/kitchen")
public class KitchenController {

    private final KitchenService kitchenService;


    @Autowired
    public KitchenController(KitchenService kitchenService) {
        // フィールドにインターセプターを設定
        this.kitchenService = kitchenService;
    }

    @GetMapping
    public Result fetAllOrders() {
        log.info("注文のオーダーをすべてフェッチする。");
        List<Order> results = kitchenService.fetAllOrders();
        log.info("フェッチ成功:{}", results);
        return Result.success(results);
    }
}
