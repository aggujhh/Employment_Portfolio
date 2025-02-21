package org.example.server.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {
    @Insert("INSERT INTO reservation(date, timeRange, name, katakana, people, content, tel, mail, specialNote) " +
            "VALUES (#{date}, #{timeRange}, #{name}, #{katakana}, #{people}, #{content}, #{tel}, #{mail}, #{specialNote})")
    void addReservation(Reservation reservation);

    @Select("SELECT * FROM reservation WHERE date BETWEEN #{firstDay} AND #{lastDay}")
    List<Reservation> fetchReservationDataByMouth(LocalDate firstDay, LocalDate lastDay);

    @Select("SELECT reserved_tables FROM reservation WHERE id=#{reservationId}")
    String fetchReservedTableIdByReservationId(Integer reservationId);

    @Update("UPDATE reservation " +
            "SET reserved_tables=#{updatedResult} " +
            "WHERE id=#{reservationId}")
    void updateReservedTableIds(String updatedResult, Integer reservationId);
}
