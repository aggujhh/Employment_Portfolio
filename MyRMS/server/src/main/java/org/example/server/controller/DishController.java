package org.example.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.Dish;
import org.example.server.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        // フィールドにインターセプターを設定
        this.dishService = dishService;
    }

    @GetMapping
    public Result getDishByCategoryId(@RequestParam("dishCategoryId") Long dishCategoryId) {
        log.info("カテゴリー{}に料理を取得する>>>", dishCategoryId);
        List<Dish> results = dishService.getDishByCategoryId(dishCategoryId);
        log.info("カテゴリー{}に料理を取得しました、結果は{}。>>>", dishCategoryId, results);
        return Result.success(results);
    }
}
