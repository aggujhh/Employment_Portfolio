package org.example.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.DishCategory;
import org.example.server.mapper.DishCategoryMapper;
import org.example.server.mapper.EmpMapper;
import org.example.server.service.DishCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DishCategoryServiceImpl implements DishCategoryService {
    private final DishCategoryMapper dishCategoryMapper;

    @Autowired
    public DishCategoryServiceImpl(DishCategoryMapper dishCategoryMapper) {
        // フィールドにインターセプターを設定
        this.dishCategoryMapper = dishCategoryMapper;
    }

    @Override
    public List<DishCategory> getDishCategories() {
        return dishCategoryMapper.fetchAllDishCategory();
    }
}
