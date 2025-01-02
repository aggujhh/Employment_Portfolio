package org.example.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.AccountingOrders;
import org.example.pojo.entity.Operations;
import org.example.pojo.entity.OrderCompletion;
import org.example.pojo.entity.OrderHistory;

import java.time.LocalDate;
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
    List<OrderCompletion> fetchAllCompletedOrders();

    @Update("UPDATE order_dish SET state='2' " +
            "WHERE order_id=#{orderId} AND dish_id=#{dishId}")
    void changeOrderDishState(OrderCompletion orderCompletion);

    @Select("SELECT COUNT(*) FROM order_dish " +
            "WHERE order_id=#{orderId} AND state IN ('0','1')")
    Integer countFinishedDish(OrderCompletion dish);

    @Select("SELECT MIN(`order`.order_time) FROM `order` " +
            "INNER JOIN desk " +
            "ON `order`.desk_id=desk.id " +
            "WHERE desk.id=#{deskId} " +
            "AND EXISTS ( " +
            "    SELECT 1 FROM order_dish " +
            "    WHERE order_dish.order_id = `order`.id " +
            "    AND order_dish.state = '1')")
    LocalDateTime fetchEarliestOrderTime(OrderCompletion orderCompletion);

    @Update("UPDATE desk SET order_time=#{orderTime} " +
            "WHERE id=#{deskId}")
    void setDeskOrderTime(OrderCompletion orderCompletion);

    @Update("UPDATE desk SET order_state='0',order_time=null " +
            "WHERE id=#{deskId}")
    void setDeskOrderStateToZero(OrderCompletion orderCompletion);


    @Update("UPDATE `order` SET state='2' WHERE id=#{id}")
    void setOrderStateToTwo(String id);

    @Select("SELECT guest_count,pay_method FROM desk " +
            "WHERE id = #{deskId} ")
    AccountingOrders fetchGuestCountById(AccountingOrders accountingOrders);

    @Select("SELECT id FROM `order` " +
            "WHERE desk_id=(#{deskId}) AND state ='3'")
    List<String> fetchAllStateThreeOrderIdsByDeskId(AccountingOrders accountingOrders);

    @Select("SELECT order_dish.dish_id,dish.name,dish.price,order_dish.count FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id=dish.id " +
            "WHERE order_dish.order_id=#{orderId}")
    List<AccountingOrders.Dishes> fetchAllDishesByOrderId(String orderId);

    @Select("SELECT id FROM `order` " +
            "WHERE desk_id=(#{deskId}) AND state !='4'")
    List<String> fetchOrderByDeskId(String deskId);

    @Update("UPDATE `order` SET state='4' WHERE desk_id=#{deskId}")
    void setOrderStateToFour(String order);

    @Update("UPDATE order_dish SET state='4' WHERE order_id=#{orderId}")
    void setOrderDishStateToFourByOrderId(String orderId);

    @Update("UPDATE desk SET guest_count=0,desk_state='0',order_state='0',order_time=null,pay_method=null " +
            "WHERE id=#{deskId}")
    void resetDeskByDeskId(OrderHistory orderHistory);

    @Select("SELECT order_dish.dish_id,order_dish.count " +
            "FROM order_dish " +
            "INNER JOIN dish " +
            "ON order_dish.dish_id = dish.id " +
            "WHERE order_dish.order_id=#{orderId} " +
            "AND order_dish.state!='4'")
    List<OrderHistory.Dishes> fetchAllStateIsNotFourOrders(String orderId);

    @Insert("INSERT INTO order_history_dish " +
            "VALUES (#{orderHistoryId},#{dishId},#{count})")
    void addNewOrderHistoryDish(String orderHistoryId, Integer dishId, Integer count);

    @Insert("INSERT INTO order_history " +
            "VALUES (#{orderHistoryId},#{payMethod},NOW(),#{amount})")
    void addNewOrderHistory(OrderHistory orderHistory);

    @Insert("INSERT INTO operations(date) VALUES (#{today})")
    void addNewOperations(LocalDate today);

    @Select("SELECT * FROM operations WHERE date=#{today}")
    Operations fetchOperations(LocalDate today);

    @Select("SELECT lunchTimeEnd, dinnerTimeEnd, lunchTimeStart, dinnerTimeStart FROM setting")
    Operations fetchEndTime();

    @Update("UPDATE operations " +
            "SET customer_count=#{customerCount},sales=#{sales},turnover_rate=#{turnoverRate} " +
            "WHERE date=#{date}")
    void setOperations(Operations operations);

    @Select("SELECT customer_count FROM operations WHERE date=#{today}")
    int getCustomerCount(LocalDate today);
}
