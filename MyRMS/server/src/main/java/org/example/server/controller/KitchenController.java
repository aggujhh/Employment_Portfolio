package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.DishCategory;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderHistory;
import org.example.pojo.entity.OrderSnapshot;
import org.example.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping
    public Result changeOrderDishState(@RequestBody Order.Dishes dish) {
        log.info("調理済み料理の状態を変更する。引数({})", dish);
        String versionCode = kitchenService.changeOrderDishState(dish);
        log.info("変更成功!");
        return Result.success(versionCode);
    }

    @PutMapping
    public Result changeOrderState(@RequestBody Order order) {
        log.info("オーダー内のすべての料理を調理済み状態に変更します。引数>>>>>>>>>>({})", order.getId());
        String versionCode = kitchenService.changeOrderState(order);
        log.info(">>>>>>>>>>>>>変更成功!");
        return Result.success(versionCode);
    }

    @PutMapping("/reset")
    public Result resetAllOrderAmdDishState() {
        log.info("すべて料理の状態を最初の状態をリセットする");
        String versionCode =kitchenService.resetAllOrderAmdDishState();
        log.info("リセット成功!,引数{}>>>>>>>>>>>>>>>>>>>>>>>>",versionCode);
        return Result.success(versionCode);
    }

    @PutMapping("/undo")
    public Result undo(@RequestBody OrderSnapshot orderSnapshot) {
        log.info("元に戻す操作,引数>>>>>>>>>>>>>>>{}", orderSnapshot);
        kitchenService.undoOrRedo(orderSnapshot);
        log.info(">>>>>>>>>>>>>>>>元に戻す操作成功!");
        return Result.success();
    }

    @PutMapping("/redo")
    public Result redo(@RequestBody OrderSnapshot orderSnapshot) {
        log.info("やり直し操作,引数>>>>>>>>>>>>>>>{}", orderSnapshot);
        kitchenService.undoOrRedo(orderSnapshot);
        log.info(">>>>>>>>>>>>>>>>やり直し操作成功!");
        return Result.success();
    }

    @PutMapping("/initialization")
    public Result initializationVersion() {
        log.info("スナップショットの初期化>>>>>>>>>>>>>>>");
        String versionCode = kitchenService.initializationVersion();
        log.info(">>>>>>>>>>>>>>>>初期化成功!,引数{}", versionCode);
        return Result.success(versionCode);
    }

    @GetMapping("/history")
    public Result fetchAllOrderHistory() {
        log.info("すべての注文履歴をフェッチする。");
        List<OrderHistory> results = kitchenService.fetchAllOrderHistory();
        log.info("フェッチ成功:{}", results);
        return Result.success(results);
    }

}
