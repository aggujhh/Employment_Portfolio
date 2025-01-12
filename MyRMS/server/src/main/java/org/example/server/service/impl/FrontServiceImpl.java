package org.example.server.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.*;
import org.example.server.mapper.DeskMapper;
import org.example.server.mapper.FrontMapper;
import org.example.server.mapper.QrCodeMapper;
import org.example.server.service.FrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FrontServiceImpl implements FrontService {

    private final FrontMapper frontMapper;
    private final DeskMapper deskMapper;

    private final QrCodeMapper qrCodeMapper;

    @Autowired
    public FrontServiceImpl(FrontMapper frontMapper, DeskMapper deskMapper, QrCodeMapper qrCodeMapper) {
        // フィールドにインターセプターを設定
        this.frontMapper = frontMapper;
        this.deskMapper = deskMapper;
        this.qrCodeMapper = qrCodeMapper;
    }

    @Override
    public List<OrderCompletion> fetchAllCompletedOrders() {
        return frontMapper.fetchAllCompletedOrders();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void changeOrderDishState(OrderCompletion orderCompletion) {
        frontMapper.changeOrderDishState(orderCompletion);
        Integer count = frontMapper.countFinishedDish(orderCompletion);
        String id = orderCompletion.getOrderId();
        if (count == 0) {
            frontMapper.setOrderStateToTwo(id);
        }
        LocalDateTime earliestOrderTime = frontMapper.fetchEarliestOrderTime(orderCompletion);
        if (earliestOrderTime != null) {
            orderCompletion.setOrderTime(earliestOrderTime);
            frontMapper.setDeskOrderTime(orderCompletion);
        } else {
            frontMapper.setDeskOrderStateToZero(orderCompletion);
        }
    }

    @Override
    public AccountingOrders fetchAllAccountingOrders(AccountingOrders accountingOrders) {
        AccountingOrders newAccountingOrders = frontMapper.fetchGuestCountById(accountingOrders);
        List<String> orderIds = frontMapper.fetchAllStateThreeOrderIdsByDeskId(accountingOrders);
        Map<Integer, AccountingOrders.Dishes> dishMap = new HashMap<>();
        orderIds.forEach(orderId -> {
            // 現在の注文IDに関連するすべての注文済み料理を取得
            List<AccountingOrders.Dishes> results = frontMapper.fetchAllDishesByOrderId(orderId);
            for (AccountingOrders.Dishes result : results) {
                Integer dishId = result.getDishId(); // 料理IDを取得
                if (dishMap.containsKey(dishId)) {
                    // 如果已存在，累计菜品的数量
                    AccountingOrders.Dishes existingDish = dishMap.get(dishId);
                    existingDish.setCount(existingDish.getCount() + result.getCount());
                } else {
                    // 如果不存在，将菜品加入到 dishMap 中
                    dishMap.put(dishId, result);
                }
            }
        });
        newAccountingOrders.setDishes(new ArrayList<>(dishMap.values()));
        int amount = 0; // 使用基本类型 int
        for (Map.Entry<Integer, AccountingOrders.Dishes> entry : dishMap.entrySet()) {
            AccountingOrders.Dishes value = entry.getValue();
            int price = value.getPrice();
            int count = value.getCount();
            amount += price * count; // 计算总金额
        }
        newAccountingOrders.setAmount(amount);
        newAccountingOrders.setDeskId(accountingOrders.getDeskId());
        return newAccountingOrders;
    }

    @Override
    public void accountingConfirmationCompleted(OrderHistory orderHistory) {
        // ランダムな注文履歴IDを生成
        String orderHistoryId = NanoIdUtils.randomNanoId();
        orderHistory.setOrderHistoryId(orderHistoryId);

        // デスクIDを取得
        String deskId = orderHistory.getDeskId();

        // デスクIDに関連するすべての注文IDを取得
        List<String> orderIds = frontMapper.fetchOrderByDeskId(deskId);

        // 料理情報を保存するためのマップ (キー: dishId, 値: Dishes オブジェクト)
        Map<Integer, OrderHistory.Dishes> dishMap = new HashMap<>();

        // すべての注文IDをループして処理
        orderIds.forEach(orderId -> {
            // 現在の注文IDに関連する"状態が4ではない"すべての料理を取得
            List<OrderHistory.Dishes> results = frontMapper.fetchAllStateIsNotFourOrders(orderId);

            for (OrderHistory.Dishes result : results) {
                Integer dishId = result.getDishId(); // 料理IDを取得

                // dishId がすでにマップに存在する場合は数量を合計
                if (dishMap.containsKey(dishId)) {
                    OrderHistory.Dishes existingDish = dishMap.get(dishId);
                    existingDish.setCount(existingDish.getCount() + result.getCount());
                } else {
                    // 存在しない場合、新しい料理をマップに追加
                    dishMap.put(dishId, result);
                }
            }

            // 現在の注文IDに関連するすべての料理の状態を"4"に更新
            frontMapper.setOrderDishStateToFourByOrderId(orderId);
        });

        // 新しい注文履歴を追加
        frontMapper.addNewOrderHistory(orderHistory);

        // マップに保存された料理情報をループして新しい注文履歴に追加
        dishMap.forEach((key, value) -> {
            Integer count = value.getCount();
            frontMapper.addNewOrderHistoryDish(orderHistoryId, key, count);
        });

        // デスクの注文状態を"4"に設定 (完了状態)
        frontMapper.setOrderStateToFour(deskId);

        // 当日の営業データを取得
        LocalDate today = LocalDate.now();
        Operations operations = frontMapper.fetchOperations(today);

        // 顧客数を更新 (現在の顧客数 + デスクのゲスト数)
        Integer customerCount = operations.getCustomerCount() + deskMapper.getGuestCount(deskId);
        operations.setCustomerCount(customerCount);

        // 売上を更新 (現在の売上 + 注文履歴の金額)
        operations.setSales(operations.getSales() + orderHistory.getAmount());

        // 総座席数を計算
        List<Integer> seatCounts = deskMapper.countAllSeat();
        int seatCount = seatCounts.stream().mapToInt(Integer::intValue).sum();

        // 回転率を計算
        System.out.println("回転率を計算>>>>>>>>>>>>>>>>>>>>>>>>>" + customerCount + "/" + seatCount);
        double turnoverRate = (double) customerCount / seatCount * 100; // 浮動小数点除算を保証
        turnoverRate = Math.round(turnoverRate * 100.0) / 100.0; // 小数点以下2桁に丸める
        operations.setTurnoverRate(turnoverRate);

        // 営業データを更新
        frontMapper.setOperations(operations);

        // デスクの状態をリセット
        frontMapper.resetDeskByDeskId(orderHistory);

        qrCodeMapper.deleteQrCode(deskId);
    }

    @Override
    public Operations fetchOperations() {
        LocalDate today = LocalDate.now();
        Operations operations = frontMapper.fetchOperations(today);
        if (operations == null) {
            frontMapper.addNewOperations(today);
            operations = frontMapper.fetchOperations(today);
        }
        Operations result = frontMapper.fetchEndTime();
        operations.setLunchTimeStart(result.getLunchTimeStart());
        operations.setLunchTimeEnd(result.getLunchTimeEnd());
        operations.setDinnerTimeStart(result.getDinnerTimeStart());
        operations.setDinnerTimeEnd(result.getDinnerTimeEnd());
        return operations;
    }
}
