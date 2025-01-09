package org.example.server.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Operations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReportMapper {
    @Select("SELECT * FROM operations WHERE date BETWEEN #{startDay} AND #{endDay}")
    List<Operations.ReportData> fetchReportDataByCountDays(LocalDate startDay, LocalDate endDay);

    @Select("SELECT dish.name, SUM(order_history_dish.count) AS total_count " +
            "FROM order_history " +
            "INNER JOIN order_history_dish " +
            "ON order_history.id = order_history_dish.order_history_id " +
            "INNER JOIN dish " +
            "ON order_history_dish.dish_id = dish.id " +
            "WHERE pay_time BETWEEN #{startDay} AND #{endDay} " +
            "GROUP BY dish.name " +
            "ORDER BY total_count DESC " +
            "LIMIT 5")
    List<Operations.TopDishes> fetchTopDishesByCountDays(LocalDateTime startDay, LocalDateTime endDay);

    @Select("SELECT sales FROM operations WHERE date=#{date}")
    Integer fetchSalesByDate(LocalDate date);

    @Select("SELECT SUM(sales) FROM operations WHERE date BETWEEN #{startDay} AND #{endDay}")
    Integer fetchSalesBetweenDate(LocalDate startDay, LocalDate endDay);

    @Select("SELECT customer_count FROM operations WHERE date=#{date}")
    Integer fetchCustomerCountByDate(LocalDate date);

    @Select("SELECT SUM(customer_count) FROM operations WHERE date BETWEEN #{startDay} AND #{endDay}")
    Integer fetchCustomerCountBetweenDate(LocalDate startDay, LocalDate endDay);
    
    @Select("SELECT turnover_rate FROM operations WHERE date=#{date}")
    Double fetchTurnoverRateByDate(LocalDate date);

    @Select("SELECT SUM(turnover_rate) FROM operations WHERE date BETWEEN #{startDay} AND #{endDay}")
    Double fetchTurnoverRateBetweenDate(LocalDate startDay, LocalDate endDay);
}
