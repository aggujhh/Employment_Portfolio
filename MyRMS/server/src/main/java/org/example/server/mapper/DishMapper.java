package org.example.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.Dish;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("SELECT * FROM dish WHERE dish_category_id=#{dishCategoryId}")
    List<Dish> getDishByCategoryId(Long dishCategoryId);
}
