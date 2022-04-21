package com.better.app.annotation.apt;


import com.better.app.meal_annotation.MealFactory;

@MealFactory(
        id = "Tiramisu",
        type = Meal.class
)
public class Tiramisu implements Meal {

    @Override
    public float price() {
        return 4.5f;
    }
}
