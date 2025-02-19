package org.example.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.Reservation;
import org.example.server.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public Result fetchReservationDataByMouth(@RequestParam("date") String date) {
        log.info("月より、予約データを所得する。引数({})>>>", date);
        List<Reservation> results = reservationService.fetchReservationDataByMouth(date);
        log.info("フェッチ成功:{}", results);
        return Result.success(results);
    }

    @PostMapping
    public Result addReservation(@Validated @RequestBody Reservation reservation) {
        log.info("予約を追加する。追加する内容は{}>>>", reservation);
        reservationService.addReservation(reservation);
        return Result.success();
    }
}
