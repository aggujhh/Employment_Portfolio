package org.example.server.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.entity.Order;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO ")
    void addOrder(Order order);
}
