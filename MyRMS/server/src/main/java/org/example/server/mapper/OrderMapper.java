package org.example.server.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Order;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO `order`(id, desk_id, state, order_time, completion_time) " +
            "VALUES (#{id},#{deskId},'0',#{orderTime},null)")
    void addOrder(Order order);

    @Insert("INSERT INTO order_dish(order_id, dish_id, count) " +
            "VALUES (#{orderId},#{dishId},#{count})")
    void addOrderDishRelation(Order.Dishes dish);

    @Select("SELECT id FROM `order` WHERE desk_id=(#{deskId})")
    List<String> fetchOrderByDeskId(String deskId);

    @Select("SELECT dish.price,order_dish.count FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id = dish.id " +
            "WHERE order_dish.order_id=#{orderId}")
    List<Map<String, Object>> fetchDeskPriceAndCountByOrderId(String orderId);

    @Select("SELECT order_dish.order_id,order_dish.dish_id,dish.name,order_dish.state,order_dish.count " +
            "FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id = dish.id " +
            "WHERE order_dish.order_id=#{orderId}")
    List<Order.Dishes> fetchAllCompletedOrders(String orderId);
}
