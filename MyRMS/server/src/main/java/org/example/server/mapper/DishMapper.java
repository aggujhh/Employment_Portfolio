package org.example.server.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.entity.Dish;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("SELECT * FROM dish WHERE dish_category_id=#{dishCategoryId}")
    List<Dish> getDishByCategoryId(Long dishCategoryId);

    @Insert("INSERT INTO dish(dish_category_id, name, price, image, description, creator, create_time, updater, update_time) " +
            "VALUES(#{dishCategoryId},#{name},#{price},#{image},#{description},#{creator},#{createTime},null,null)")
    void addDish(Dish dish);

    @Update("UPDATE dish SET image=#{image}, updater=#{updater},update_time=#{updateTime} WHERE id=${id}")
    void updateDishImage(Dish dish);

    @Update("UPDATE dish " +
            "SET name=#{name},price=#{price},description=#{description},state=#{state}, updater=#{updater},update_time=#{updateTime} " +
            "WHERE id=${id}")
    void updateDishInfo(Dish dish);

    @Delete("DELETE FROM dish WHERE id=#{id}")
    void deleteDish(Dish dish);
}
