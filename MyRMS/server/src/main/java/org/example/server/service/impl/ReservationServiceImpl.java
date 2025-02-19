package org.example.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Reservation;
import org.example.server.mapper.ReservationMapper;
import org.example.server.service.ReservationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Override
    public void addReservation(Reservation reservation) {
        reservationMapper.addReservation(reservation);
    }

    @Override
    public List<Reservation> fetchReservationDataByMouth(String date) {
        // 日付フォーマットを定義する（"yyyy-MM-dd" 形式）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 文字列の日付を LocalDate 型に変換する
        LocalDate givenDate = LocalDate.parse(date, formatter);

        // 指定された日付の月の最初の日を取得する
        LocalDate firstDay = givenDate.withDayOfMonth(1);

        // 指定された日付の月の最後の日を取得する
        LocalDate lastDay = givenDate.withDayOfMonth(givenDate.lengthOfMonth());

        // 予約データを取得して返す
        return reservationMapper.fetchReservationDataByMouth(firstDay, lastDay);
    }
}
