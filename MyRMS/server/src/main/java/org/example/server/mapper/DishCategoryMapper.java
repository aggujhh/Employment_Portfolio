package org.example.server.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.entity.DishCategory;

import java.util.List;

@Mapper
public interface DishCategoryMapper {
    //料理のカテゴリーを全部するフェッチする
    @Select("SELECT * FROM dish_category")
    List<DishCategory> fetchAllDishCategory();

    @Insert("INSERT INTO dish_category(name, creator, create_time, updater, update_time) " +
            "VALUES (#{name},#{creator},#{createTime},null,null)")
    void addDishCategory(DishCategory dishCategory);

    @Delete("DELETE FROM dish_category WHERE id=#{id}")
    void deleteDishCategory(DishCategory dishCategory);

    @Update("UPDATE dish_category " +
            "SET name=#{name},updater=#{updater},update_time=#{updateTime} " +
            "WHERE id=#{id}")
    void updateDishCategory(DishCategory dishCategory);
}


