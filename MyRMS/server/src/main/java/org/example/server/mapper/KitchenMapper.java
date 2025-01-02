package org.example.server.mapper;


import org.apache.ibatis.annotations.*;
import org.example.pojo.entity.Order;
import org.example.pojo.entity.OrderCompletion;
import org.example.pojo.entity.OrderSnapshot;

import java.util.List;

@Mapper
public interface KitchenMapper {

    @Select("SELECT `order`.*,desk.guest_count FROM `order` " +
            "INNER JOIN desk " +
            "ON `order`.desk_id = desk.id ")
    List<Order> fetAllOrders();

    @Select("SELECT order_dish.order_id,order_dish.dish_id,dish.name,order_dish.state,order_dish.count " +
            "FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id = dish.id " +
            "WHERE order_dish.order_id=#{orderId}")
    List<Order.Dishes> fetDishesByOrderId(String orderId);

    @Update("UPDATE order_dish SET state='1',completion_time=NOW() " +
            "WHERE order_id=#{orderId} AND dish_id=#{dishId}")
    void changeOrderDishState(Order.Dishes dish);

    @Select("SELECT COUNT(*) FROM order_dish " +
            "WHERE order_id=#{orderId} AND state='0'")
    Integer countCookedDish(Order.Dishes dish);

    @Update("UPDATE `order` SET state='1' WHERE id=#{id}")
    void changeOrderState(String id);

    @Update("UPDATE `order` SET state='0' WHERE state='1'")
    void resetAllOrderState();

    @Update("UPDATE order_dish SET state='0' WHERE state='1'")
    void resetAllOrderDishState();

    @Update("UPDATE desk SET order_state='0' WHERE order_state='1'")
    void resetAllDeskState();

    @Insert("INSERT INTO order_snapshot (version,order_id, dish_id, order_state, dish_state) " +
            "SELECT #{version},order_dish.order_id, order_dish.dish_id, `order`.state, order_dish.state " +
            "FROM `order` " +
            "INNER JOIN order_dish " +
            "ON `order`.id = order_dish.order_id")
    void saveOrderSnapshot(@Param("version") String version);

    @Select("SELECT COUNT(DISTINCT version) FROM order_snapshot")
    Integer countVersion();

    @Delete("DELETE FROM order_snapshot " +
            "WHERE created_at IN (" +
            "    SELECT created_at FROM (" +
            "             SELECT created_at " +
            "             FROM order_snapshot" +
            "             ORDER BY created_at " +
            "             LIMIT 1" +
            "         ) AS oldest" +
            ")")
    void deleteFirstSnapshot();

    @Select("SELECT DISTINCT order_id,order_state FROM order_snapshot WHERE version=#{version}")
    List<OrderSnapshot> fetchOrderIdAndOrderStateByVersion(OrderSnapshot orderSnapshot);

    @Update("UPDATE `order` SET state=#{orderState} " +
            "WHERE id=#{orderId}")
    void undoOrRedoOrder(OrderSnapshot orderSnapshot);

    @Select("SELECT DISTINCT order_id,dish_id,dish_state FROM order_snapshot WHERE version=#{version}")
    List<OrderSnapshot> fetchDishIdAndDishStateByVersion(OrderSnapshot orderSnapshot);

    @Update("UPDATE order_dish SET state=#{dish.dishState} " +
            "WHERE order_id=#{orderId} AND dish_id=#{dish.dishId}")
    void undoOrRedoDish(String orderId, OrderSnapshot dish);

    @Update("UPDATE order_dish SET state='1',completion_time=NOW() " +
            "WHERE order_id=#{id}")
    void changeAllDishStateByOrderId(String id);

    @Select("SELECT order_dish.order_id,`order`.desk_id,dish.name AS 'dishName',order_dish.count,`order`.order_time, order_dish.completion_time " +
            "FROM order_dish " +
            "INNER JOIN `order` " +
            "ON order_dish.order_id = `order`.id " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id=dish.id " +
            "WHERE order_dish.state != '0' " +
            "ORDER BY order_dish.completion_time DESC ")
    List<OrderCompletion> fetchAllOrderHistory();

    @Update("UPDATE desk SET desk_state='1',order_state='1' WHERE id=#{deskId}")
    void setTableOrderState(String deskId);

    @Select("SELECT desk_id FROM `order` WHERE id=#{id}")
    String getDeskIdByOrderId(String id);

}
