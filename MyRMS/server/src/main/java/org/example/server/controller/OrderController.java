package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.Dish;
import org.example.pojo.entity.DishCategory;
import org.example.server.service.DishCategoryService;
import org.example.server.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final DishCategoryService dishCategoryService;
    private final DishService dishService;

    @Autowired
    public OrderController(DishCategoryService dishCategoryService, DishService dishService) {
        // フィールドにインターセプターを設定
        this.dishCategoryService = dishCategoryService;
        this.dishService = dishService;
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
}
