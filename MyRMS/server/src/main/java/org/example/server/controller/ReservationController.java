package org.example.server.controller;
import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.pojo.entity.Reservation;
import org.example.server.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Result addReservation(@Validated @RequestBody Reservation reservation) {
        log.info("予約を追加する。追加する内容は{}>>>", reservation);
        reservationService.addReservation(reservation);
        return Result.success();
    }
}
