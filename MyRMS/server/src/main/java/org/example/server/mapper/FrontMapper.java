package org.example.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.OrderHistory;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FrontMapper {
    @Select("SELECT order_dish.order_id,`order`.desk_id,order_dish.dish_id,dish.name AS 'dishName',order_dish.count " +
            "FROM order_dish " +
            "INNER JOIN `order` " +
            "ON order_dish.order_id = `order`.id " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id=dish.id " +
            "WHERE order_dish.state = '1' " +
            "ORDER BY order_dish.completion_time")
    List<OrderHistory> fetchAllCompletedOrders();

    @Update("UPDATE order_dish SET state='2' " +
            "WHERE order_id=#{orderId} AND dish_id=#{dishId}")
    void changeOrderDishState(OrderHistory orderHistory);

    @Select("SELECT MIN(`order`.order_time) FROM `order` " +
            "INNER JOIN desk " +
            "ON `order`.desk_id=desk.id " +
            "WHERE desk.id=#{deskId} " +
            "AND EXISTS ( " +
            "    SELECT 1 FROM order_dish " +
            "    WHERE order_dish.order_id = `order`.id " +
            "    AND order_dish.state = '1')")
    LocalDateTime fetchEarliestOrderTime(OrderHistory orderHistory);

    @Update("UPDATE desk SET order_time=#{orderTime} " +
            "WHERE id=#{deskId}")
    void setDeskOrderTime(OrderHistory orderHistory);

    @Update("UPDATE desk SET order_state='0',order_time=null " +
            "WHERE id=#{deskId}")
    void setDeskOrderStateToZero(OrderHistory orderHistory);
}
