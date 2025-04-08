package org.example.server.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.Desk;
import org.example.pojo.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface DeskMapper {
    //テーブルを全部するフェッチする
    @Select("SELECT * FROM desk")
    List<Desk> fetchAllTables();

    @Select("SELECT * FROM desk WHERE id=#{id}")
    Desk fetchDateByTableId(Desk desk);

    @Update("UPDATE desk SET guest_count=#{guestCount},desk_state='1' WHERE id=#{id}")
    void setCustomerCount(Desk desk);

    @Update("UPDATE desk SET guest_count=0,desk_state='0',order_state='0',order_time=null")
    void resetAllTables();

    @Select("SELECT seat_count FROM desk")
    List<Integer> countAllSeat();

    @Select("SELECT guest_count FROM desk WHERE id=#{deskId}")
    int getGuestCount(String deskId);

    @Select("SELECT order_state FROM desk WHERE id=#{deskId}")
    String getDeskOrderState(String deskId);

    @Update("UPDATE desk SET desk.order_state=#{orderState} WHERE id=#{deskId}")
    void setDeskOrderState(String deskId, String orderState);

    @Update("UPDATE desk SET desk_state='3' WHERE id=#{id}")
    void disableDeskById(Desk desk);

    @Update("UPDATE desk SET desk_state='0' WHERE id=#{id}")
    void restoreDeskById(Desk desk);

    @Update("UPDATE desk SET desk_state='2' WHERE id=#{id}")
    void reservedDeskById(String id);

    @Update("UPDATE desk SET desk_state='0' WHERE desk_state='2'")
    void reReservedDeskById();

    @Select("SELECT * FROM desk WHERE desk_state !='3'")
    List<String> fetchAllAvailableTableIds();

    @Select("SELECT reserved_tables FROM reservation " +
            "WHERE date = #{date} " +
            "AND timeRange IN ('11:00~12:00','12:00~13:00','13:00~14:00')")
    List<String> checkReservedTablesForMorning(LocalDate date);

    @Select("SELECT reserved_tables FROM reservation " +
            "WHERE date = #{date} " +
            "AND timeRange IN ('17:00~18:00','18:00~19:00','19:00~20:00')")
    List<String> checkReservedTablesForEvening(LocalDate date);
}
