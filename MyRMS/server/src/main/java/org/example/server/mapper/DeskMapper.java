package org.example.server.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.Desk;
import org.example.pojo.entity.DishCategory;

import java.util.List;

@Mapper
public interface DeskMapper {
    //テーブルを全部するフェッチする
    @Select("SELECT * FROM desk")
    List<Desk> fetchAllTables();

    @Select("SELECT * FROM desk WHERE id=#{id}")
    Desk fetchDateByTableId(Desk desk);

    @Update("UPDATE desk SET guest_count=#{guestCount} WHERE id=#{id}")
    void setCustomerCount(Desk desk);
}
