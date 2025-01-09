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
}
