package com.better.app.annotation.apt;

import com.better.app.meal_annotation.MealFactory;

@MealFactory(
        id = "FishBall",
        type = Meal.class
)
public class FishBall implements Meal {

    @Override
    public float price() {
        return 8.5f;
    }
}
