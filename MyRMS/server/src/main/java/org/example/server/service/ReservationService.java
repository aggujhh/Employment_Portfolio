package org.example.server.service;

import org.example.pojo.entity.Reservation;

import java.util.List;

public interface ReservationService {
    void addReservation(Reservation reservation);

    List<Reservation> fetchReservationDataByMouth(String date);

    List<String> addReservedTableId(Integer reservationId, String deskId);

    void deleteSelectedTableById(Integer reservationId, String deskId);

    void changeVisitStatus(Integer reservationId, String guestState);
}
