package com.better.app.annotation.apt;

import com.better.app.meal_annotation.MealFactory;


@MealFactory(
    id = "LemonTea",
    type = Meal.class
)
public class LemonTea implements Meal {

  @Override public float price() {
    return 6f;
  }
}
