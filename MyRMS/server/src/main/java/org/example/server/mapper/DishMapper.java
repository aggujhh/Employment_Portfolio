package org.example.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.pojo.entity.Dish;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("SELECT * FROM dish WHERE dish_category_id=#{dishCategoryId}")
    List<Dish> getDishByCategoryId(Long dishCategoryId);

    @Insert("INSERT INTO dish(dish_category_id, name, price, image, description, creator, create_time, updater, update_time) " +
            "VALUES(#{dishCategoryId},#{name},#{price},#{image},#{description},#{creator},#{createTime},null,null)")
    void addDish(Dish dish);

    @Update("UPDATE dish SET updater=#{updater},update_time=#{updateTime} WHERE id=${id}")
    void updateDishImage(Dish dish);
}
