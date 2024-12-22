package org.example.server.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Order;

import java.util.List;

@Mapper
public interface KitchenMapper {

    @Select("SELECT `order`.*,desk.guest_count FROM `order` " +
            "INNER JOIN desk " +
            "ON `order`.desk_id = desk.id ")
    List<Order> fetAllOrders();

    @Select("SELECT dish.id,dish.name,order_dish.state,order_dish.count " +
            "FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id = dish.id " +
            "WHERE order_dish.order_id=#{orderId}")
    List<Order.Dishes> fetDishesByOrderId(String orderId);
}
