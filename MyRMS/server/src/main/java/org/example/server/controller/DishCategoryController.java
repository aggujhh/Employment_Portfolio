package org.example.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.DishCategory;
import org.example.pojo.entity.Emp;

import java.util.List;

import org.example.server.service.DishCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/dish_category")
public class DishCategoryController {
    private final DishCategoryService dishCategoryService;

    @Autowired
    public DishCategoryController(DishCategoryService dishCategoryService) {
        this.dishCategoryService = dishCategoryService;
    }

    @GetMapping
    public Result getDishCategories() {
        List<DishCategory> results = dishCategoryService.getDishCategories();
        log.info("料理のカテゴリー全部をフェッチする:{}", results);
        return Result.success(results);
    }

    @PostMapping
    public Result addDishCategory(@RequestBody DishCategory dishCategory) {
        log.info("新しい料理のカテゴリーを追加する:{}", dishCategory);
        dishCategoryService.addDishCategory(dishCategory);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteDishCategory(@RequestBody DishCategory dishCategory) {
        log.info("料理のカテゴリーを削除する:{}", dishCategory);
        dishCategoryService.deleteDishCategory(dishCategory);
        return Result.success();
    }

    @PutMapping
    public Result updateDishCategory(@RequestBody DishCategory dishCategory) {
        log.info("料理のカテゴリーを修正する:{}", dishCategory);
        dishCategoryService.updateDishCategory(dishCategory);
        return Result.success();
    }
}
