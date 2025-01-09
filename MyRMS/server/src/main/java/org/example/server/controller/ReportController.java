package org.example.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.common.SseService;
import org.example.pojo.entity.AccountingOrders;
import org.example.pojo.entity.Operations;
import org.example.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public Result fetchReportDataByCountDays(@RequestParam("countDays") Integer countDays) {
        log.info("統計データをフェッチする。引数{}", countDays);
        Operations.ReportData result = reportService.fetchReportDataByCountDays(countDays);
        log.info("フェッチ成功:{}", result);
        return Result.success(result);
    }

}
