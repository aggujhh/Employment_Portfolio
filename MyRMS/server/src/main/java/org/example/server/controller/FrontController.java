package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.common.SseService;
import org.example.pojo.entity.*;
import org.example.server.service.DeskService;
import org.example.server.service.FrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/front")
public class FrontController {

    private final FrontService frontService;
    private final SseService sseService;
    private final DeskService deskService;

    @Autowired
    public FrontController(FrontService frontService, SseService sseService, DeskService deskService) {
        // フィールドにインターセプターを設定
        this.frontService = frontService;
        this.sseService = sseService;
        this.deskService = deskService;
    }

    //SSEインターフェース
    @GetMapping(value = "/order", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamToOrder() {
        return sseService.getOrderSseFlux();
    }

    @GetMapping
    public Result fetchAllCompletedOrders() {
        log.info("調理済オーダーをすべてフェッチ。");
        List<OrderCompletion> results = frontService.fetchAllCompletedOrders();
        log.info("フェッチ成功:{}", results);
        return Result.success(results);
    }

    @GetMapping("/accounting")
    public Result fetchAllAccountingOrders(@RequestParam("deskId") String deskId) {
        AccountingOrders accountingOrders = new AccountingOrders();
        accountingOrders.setDeskId(deskId);
        log.info("会計中のオーダーをすべてフェッチ。引数{}", accountingOrders);
        AccountingOrders result = frontService.fetchAllAccountingOrders(accountingOrders);
        log.info("フェッチ成功:{}", result);
        return Result.success(result);
    }

    @PatchMapping
    public Result changeOrderDishState(@RequestBody OrderCompletion orderCompletion) {
        log.info("提供済み料理の状態を変更する。引数({})>>>>>>>>>>>>", orderCompletion);
        frontService.changeOrderDishState(orderCompletion);
        log.info(">>>>>>>>>>>>変更成功！");
        sseService.sendToOrderClients("提供済み");
        return Result.success();
    }

    @PutMapping
    public Result accountingConfirmationCompleted(@RequestBody OrderHistory orderHistory) {
        log.info("会計確認完了。引数({})>>>>>>>>>>>>", orderHistory);
        frontService.accountingConfirmationCompleted(orderHistory);
        sseService.sendToOrderClients("リセット");
        log.info(">>>>>>>>>>>>変更成功！");
        return Result.success();
    }

    @GetMapping("/operations")
    public Result fetchOperations() {
        log.info("営業情報所得する。>>>>>>>>>>>>");
        Operations result = frontService.fetchOperations();
        log.info("フェッチ成功:{}", result);
        return Result.success(result);
    }

    @PatchMapping("/checkedCall")
    public Result setDeskOrderStateComeBack(@RequestBody Desk desk) {
        String deskId = desk.getId();
        log.info("呼び出しの状況を戻る:{}", deskId);
        deskService.setDeskOrderStateComeBack(deskId);
        log.info("戻る成功しました。");
        sseService.sendToOrderClients("呼び出しの状況を戻る成功");
        return Result.success();
    }
}
