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

    @PostMapping
    public Result addDish(@RequestBody Dish dish) {
        log.info("料理を追加する。カテゴリーID{}>>>", dish.getDishCategoryId());
        dishService.addDish(dish);
        return Result.success();
    }

    @PatchMapping
    public Result updateDishImage(@RequestBody Dish dish) {
        log.info("料理の写真を変換する。写真名{}>>>", dish.getName());
        dishService.updateDishImage(dish);
        return Result.success();
    }

    @PutMapping
    public Result updateDishInfo(@RequestBody Dish dish) {
        log.info("料理の情報を修正する。引数：{}>>>", dish);
        dishService.updateDishInfo(dish);
        return Result.success();
    }
}
