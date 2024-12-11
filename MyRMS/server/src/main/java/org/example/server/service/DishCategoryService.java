package org.example.server.service;

import java.util.List;
import org.example.pojo.entity.DishCategory;

public interface DishCategoryService {
    List<DishCategory> getDishCategories();

    void addDishCategory(DishCategory dishCategory);

    void deleteDishCategory(DishCategory dishCategory);

    void updateDishCategory(DishCategory dishCategory);
}
