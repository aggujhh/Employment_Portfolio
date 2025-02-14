package org.example.server.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.entity.Reservation;

@Mapper
public interface ReservationMapper {
    @Insert("INSERT INTO reservation(date, timeRange, name, katakana, content, tel, mail, specialNote) " +
            "VALUES (#{date}, #{timeRange}, #{name}, #{katakana}, #{content}, #{tel}, #{mail}, #{specialNote})")
    void addReservation(Reservation reservation);
}
