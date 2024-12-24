package org.example.server.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderHistory;

import java.util.List;

@Mapper
public interface FrontMapper {
    @Select("SELECT `order`.desk_id,dish.name AS 'dishName',order_dish.count " +
            "FROM order_dish " +
            "INNER JOIN `order` " +
            "ON order_dish.order_id = `order`.id " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id=dish.id " +
            "WHERE order_dish.state = '1' " +
            "ORDER BY order_dish.completion_time")
    List<OrderHistory> fetchAllCompletedOrders();
}
