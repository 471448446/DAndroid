package com.better.app.annotation.apt;

import com.better.app.meal_annotation.MealFactory;

@MealFactory(
        type = Meal.class,
        id = "Pizza"
)
public class Pizza implements Meal {
    @Override
    public float price() {
        return 5f;
    }
}
