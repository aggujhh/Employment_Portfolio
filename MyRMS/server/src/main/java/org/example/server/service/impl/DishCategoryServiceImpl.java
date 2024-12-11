package org.example.server.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.DishCategory;
import org.example.server.mapper.DishCategoryMapper;
import org.example.server.mapper.EmpMapper;
import org.example.server.service.DishCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DishCategoryServiceImpl implements DishCategoryService {
    private final DishCategoryMapper dishCategoryMapper;
    private final HttpServletRequest request;

    @Autowired
    public DishCategoryServiceImpl(DishCategoryMapper dishCategoryMapper, HttpServletRequest request) {
        // フィールドにインターセプターを設定
        this.dishCategoryMapper = dishCategoryMapper;
        this.request = request;
    }


    @Override
    public List<DishCategory> getDishCategories() {
        return dishCategoryMapper.fetchAllDishCategory();
    }

    @Override
    public void addDishCategory(DishCategory dishCategory) {
        String userId = (String) request.getAttribute("userId");
        dishCategory.setCreator(userId);
        dishCategory.setCreateTime(LocalDateTime.now());
        dishCategoryMapper.addDishCategory(dishCategory);
    }

    @Override
    public void deleteDishCategory(DishCategory dishCategory) {
        dishCategoryMapper.deleteDishCategory(dishCategory);
    }

    @Override
    public void updateDishCategory(DishCategory dishCategory) {
        String userId = (String) request.getAttribute("userId");
        dishCategory.setUpdater(userId);
        dishCategory.setUpdateTime(LocalDateTime.now());
        dishCategoryMapper.updateDishCategory(dishCategory);
    }
}
