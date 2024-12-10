package org.example.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.entity.DishCategory;

import java.util.List;

@Mapper
public interface DishCategoryMapper {
    //料理のカテゴリーを全部するフェッチする
    @Select("SELECT * FROM dish_category")
    List<DishCategory> fetchAllDishCategory();
}
