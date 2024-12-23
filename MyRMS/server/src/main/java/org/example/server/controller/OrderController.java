package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.common.SseService;
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
import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final DishCategoryService dishCategoryService;
    private final DishService dishService;
    private final DeskService deskService;
    private final OrderService orderService;
    private final SseService sseService;


    @Autowired
    public OrderController(DishCategoryService dishCategoryService,
                           DishService dishService,
                           DeskService deskService,
                           OrderService orderService,
                           SseService sseService) {
        // フィールドにインターセプターを設定
        this.dishCategoryService = dishCategoryService;
        this.dishService = dishService;
        this.deskService = deskService;
        this.orderService = orderService;
        this.sseService = sseService;
    }

    // SSE 接口，厨房连接实时接收订单
    @GetMapping(value = "/kitchen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamToKitchen() {
        return sseService.getKitchenSseFlux();
    }

// SSE 接口，前台连接实时接收订单
//    @GetMapping(value = "/front", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> streamToFrontDesk() {
//        return sseService.getFrontDeskSseFlux();
//    }

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
        // 推送订单到厨房和前台
        sseService.sendToKitchen("新しいオーダーを追加");
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
