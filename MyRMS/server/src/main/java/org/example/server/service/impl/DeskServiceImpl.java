package org.example.server.service.impl;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Desk;
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



    @Autowired
    public DeskServiceImpl(DeskMapper deskMapper) {
        // フィールドにインターセプターを設定
        this. deskMapper =  deskMapper;
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
}
