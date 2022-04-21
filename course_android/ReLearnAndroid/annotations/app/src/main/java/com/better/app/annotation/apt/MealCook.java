package com.better.app.annotation.apt;


public class MealCook {
    public static Meal cook(String id) {
        if ("FishBall".equals(id)) {
            return new FishBall();
        }
        if ("LemonTea".equals(id)) {
            return new LemonTea();
        }
        if ("Tiramisu".equals(id)) {
            return new Tiramisu();
        }
        // 如新增其它的meal，需要修改该类，在这里增加if-else。不利于维护，破坏了开闭原则。
        return null;
    }

    public Meal create(String id) {
        return MealFactory.create(id);
    }
}
