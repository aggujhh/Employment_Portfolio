package org.example.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.Desk;
import org.example.server.service.DeskService;
import org.example.server.service.DishCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/desk")
public class DeskController {

    private final DeskService deskService;

    @Autowired
    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public Result fetchAllTables() {
        List<Desk> results = deskService.fetchAllTables();
        log.info("テーブルをすべてフェッチする:{}", results);
        return Result.success(results);
    }

    @PutMapping
    public Result resetAllTables() {
        log.info("テーブルをすべてリセットする。");
        deskService.resetAllTables();
        return Result.success();
    }
}
