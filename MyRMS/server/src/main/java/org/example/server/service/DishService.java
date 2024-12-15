package org.example.server.service;

import org.example.pojo.entity.Dish;

import java.util.List;

public interface DishService {
    List<Dish> getDishByCategoryId(Long dish);

    void addDish(Dish dish);

    void updateDishImage(Dish dish);

    void updateDishInfo(Dish dish);

    void deleteDish(Dish dish);
}
