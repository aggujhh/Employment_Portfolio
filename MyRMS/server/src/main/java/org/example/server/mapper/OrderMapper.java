package org.example.server.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderHistory;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO `order`(id, desk_id, state, order_time) " +
            "VALUES (#{id},#{deskId},'0',#{orderTime})")
    void addOrder(Order order);

    @Insert("INSERT INTO order_dish(order_id, dish_id, count) " +
            "VALUES (#{orderId},#{dishId},#{count})")
    void addOrderDishRelation(Order.Dishes dish);

    @Select("SELECT id FROM `order` " +
            "WHERE desk_id=(#{deskId}) AND state !='4'")
    List<String> fetchOrderByDeskId(String deskId);

    @Select("SELECT dish.price,order_dish.count FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id = dish.id " +
            "WHERE order_dish.order_id=#{orderId} " +
            "AND order_dish.state !='4'")
    List<Map<String, Object>> fetchDeskPriceAndCountByOrderId(String orderId);

    @Select("SELECT order_dish.order_id,order_dish.dish_id,dish.name,order_dish.state,order_dish.count " +
            "FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id = dish.id " +
            "WHERE order_dish.order_id=#{orderId} " +
            "AND order_dish.state!='4' " +
            "ORDER BY order_dish.state DESC")
    List<Order.Dishes> fetchAllCompletedOrders(String orderId);

    @Select("SELECT EXISTS (SELECT 1 " +
            "FROM desk " +
            "WHERE id = #{deskId} " +
            "AND order_time IS NOT NULL)")
    boolean checkDeskHasOrderTime(String deskId);

    @Update("UPDATE desk " +
            "SET desk_state='1' " +
            "WHERE id=#{deskId} AND desk_state='0'")
    void setNewDeskState(String deskId);

    @Update("UPDATE desk " +
            "SET desk_state='1',order_time=NOW() " +
            "WHERE id=#{deskId}")
    void setNewDeskStateAndOrderTime(String deskId);

    @Update("UPDATE `order` SET state='3' WHERE desk_id=#{deskId} AND state!='4'")
    void setOrderStateToThree(String order);

    @Update("UPDATE order_dish SET state='3' WHERE order_id=#{orderId} AND state!='4'")
    void setOrderDishStateToThreeByOrderId(String orderId);

    @Update("UPDATE desk SET desk.order_state='2',order_time=null,pay_method=#{payMethod} " +
            "WHERE id=#{deskId}")
    void setDeskOrderStateToTwo(OrderHistory order);
}
