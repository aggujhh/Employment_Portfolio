package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.Desk;
import org.example.pojo.entity.Dish;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.DishCategory;
import org.example.server.service.DeskService;
import org.example.server.service.DishCategoryService;
import org.example.server.service.DishService;
import org.example.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final DishCategoryService dishCategoryService;
    private final DishService dishService;
    private final DeskService deskService;
    private final OrderService orderService;


    @Autowired
    public OrderController(DishCategoryService dishCategoryService,
                           DishService dishService,
                           DeskService deskService,
                           OrderService orderService) {
        // フィールドにインターセプターを設定
        this.dishCategoryService = dishCategoryService;
        this.dishService = dishService;
        this.deskService = deskService;
        this.orderService = orderService;
    }

    @GetMapping
    public Result getDishCategories() {
        List<DishCategory> results = dishCategoryService.getDishCategories();
        log.info("料理のカテゴリー全部をフェッチする:{}", results);
        return Result.success(results);
    }

    @GetMapping("/{dishCategoryId}")
    public List<Dish> getDishesByCategoryId(@PathVariable("dishCategoryId") Long dishCategoryId) {
        // 假设有一个服务层处理业务逻辑
        return dishService.getDishByCategoryId(dishCategoryId);
    }

    @GetMapping("/desk/all")
    public Result fetchAllTables() {
        List<Desk> results = deskService.fetchAllTables();
        log.info("テーブルをすべてフェッチする:{}", results);
        return Result.success(results);
    }

    @GetMapping("/desk")
    public Result fetchDateByTableId(@RequestParam("id") String id) {
        Desk desk = new Desk();
        desk.setId(id);
        log.info("テーブルによりデータをフェッチ:{}", desk);
        Desk result = deskService.fetchDateByTableId(desk);
        log.info("フェッチした結果:{}", result);
        return Result.success(result);
    }

    @PatchMapping("/desk")
    public Result setCustomerCount(@RequestBody Desk desk) {
        log.info("テーブル人数を保存する:{}", desk);
        deskService.setCustomerCount(desk);
        log.info("保存成功しました。");
        return Result.success();
    }

    @PutMapping
    public Result addOrder(@RequestBody Order order) {
        log.info("オーダーを追加する:{}", order);
        orderService.addOrder(order);
        log.info("保存成功しました。");
        return Result.success();
    }

    @GetMapping("/price")
    public Result getOrderTotalPrice(@RequestParam("deskId") String deskId) {
        log.info("オーダーの総値段を取得する:{}", deskId);
        Integer result = orderService.getOrderTotalPrice(deskId);
        log.info("取得成功しました。");
        return Result.success(result);
    }

    @GetMapping("/completed")
    public Result fetchAllCompletedOrders(@RequestParam("deskId") String deskId) {
        log.info("すべて注文完了オーダーを取得する");
        List<Order.Dishes> result = orderService.fetchAllCompletedOrders(deskId);
        log.info("取得成功しました。");
        return Result.success(result);
    }
}
