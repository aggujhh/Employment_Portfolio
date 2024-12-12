package org.example.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Dish;
import org.example.server.mapper.DishMapper;
import org.example.server.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {
    private final DishMapper dishMapper;

    @Autowired
    public DishServiceImpl(DishMapper dishMapper) {
        // フィールドにインターセプターを設定
        this.dishMapper = dishMapper;
    }

    @Override
    public List<Dish> getDishByCategoryId(Long dishCategoryId) {
        return dishMapper.getDishByCategoryId(dishCategoryId);
    }
}
