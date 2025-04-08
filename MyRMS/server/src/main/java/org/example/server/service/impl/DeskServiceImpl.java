package org.example.server.service.impl;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Desk;
import org.example.server.manager.DeskStateManager;
import org.example.server.mapper.DeskMapper;
import org.example.server.mapper.DishMapper;
import org.example.server.service.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class DeskServiceImpl implements DeskService {
    private final DeskMapper deskMapper;
    private final DeskStateManager deskStateManager;


    @Autowired
    public DeskServiceImpl(DeskMapper deskMapper, DeskStateManager deskStateManager) {
        // フィールドにインターセプターを設定
        this.deskMapper = deskMapper;
        this.deskStateManager = deskStateManager;
    }


    @Override
    public List<Desk> fetchAllTables() {
        // 現在の日付と時刻を取得
        LocalDate date = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 午前の時間帯を定義（11:00〜14:00）
        LocalTime amStart = LocalTime.of(11, 0);
        LocalTime amEnd = LocalTime.of(14, 0);

        // 午後の時間帯を定義（17:00〜20:00）
        LocalTime pmStart = LocalTime.of(17, 0);
        LocalTime pmEnd = LocalTime.of(20, 0);

        List<String> tables = null; // データベースから取得された予約済みテーブル（カンマ区切り文字列）
        List<String> tableList = new ArrayList<>(); // 分解後のテーブルIDリスト

        // 現在の時間が午前の時間帯かどうかを確認
        if (now.isAfter(amStart) && now.isBefore(amEnd)) {
            tables = deskMapper.checkReservedTablesForMorning(date);
        }
        // 現在の時間が午後の時間帯かどうかを確認
        else if (now.isAfter(pmStart) && now.isBefore(pmEnd)) {
            tables = deskMapper.checkReservedTablesForEvening(date);
        }

        // 予約されたテーブルリストが存在する場合
        if (tables != null) {
            tables.forEach(table -> {
                // 空文字列はスキップ
                if (table == null || table.isEmpty()) {
                    return;
                }

                // カンマで分割して個別のテーブルIDに分解
                String[] splitItems = table.split(",");

                // 重複を避けてtableListに追加
                for (String item : splitItems) {
                    if (!tableList.contains(item)) {
                        tableList.add(item);
                    }
                }
            });
        }

        // 予約されたテーブルIDに基づき処理を行う
        deskMapper.reReservedDeskById();
        for (String item : tableList) {
            deskMapper.reservedDeskById(item);
        }

        // 全テーブル情報を取得して返す
        return deskMapper.fetchAllTables();
    }

    @Override
    public Desk fetchDateByTableId(Desk desk) {
        return deskMapper.fetchDateByTableId(desk);
    }

    @Override
    public void setCustomerCount(Desk desk) {
        deskMapper.setCustomerCount(desk);
    }

    @Override
    public void resetAllTables() {
        deskMapper.resetAllTables();
    }

    @Override
    public void setDeskOrderStateForThree(String deskId) {
        // 获取当前状态并保存到状态管理器
        String previousState = deskMapper.getDeskOrderState(deskId);
        deskStateManager.savePreviousState(deskId, previousState);
        String orderState = "3";
        deskMapper.setDeskOrderState(deskId, orderState);
    }

    @Override
    public void setDeskOrderStateComeBack(String deskId) {
        // 从状态管理器中获取旧状态并恢复
        String previousState = deskStateManager.getPreviousState(deskId);
        if (previousState == null) {
            previousState = "0";
        }
        deskMapper.setDeskOrderState(deskId, previousState);
        deskStateManager.clearPreviousState(deskId); // 恢复后清除
    }

    @Override
    public void disableDeskById(Desk desk) {
        deskMapper.disableDeskById(desk);
    }

    @Override
    public void restoreDeskById(Desk desk) {
        deskMapper.restoreDeskById(desk);
    }
}
