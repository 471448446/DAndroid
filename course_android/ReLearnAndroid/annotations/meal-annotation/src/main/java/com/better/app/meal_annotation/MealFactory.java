package com.better.app.meal_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于类上面的标记，用于生成实例
 */
@Target(ElementType.TYPE)
/**
 *  因为只是在javac编译期间使用，创建为Factory类之后，就不需要该注解了。所以保留级别为源码级别。
 *  同时因为是源码级别，还没到混淆的那一步，所以也可以不用考虑混淆
 */
@Retention(RetentionPolicy.SOURCE)
public @interface MealFactory {
    /**
     * 用于划分生成的代码属于哪一个Factory
     * 代码会依据该类名称，生成 XXXFactory
     *
     * @return 父类的Class
     */
    Class<?> type();

    /**
     * 一个名称用于区别，使用{@link MealFactory}时，应该归属于哪个类{@link #type()}下面
     *
     * @return 唯一ID
     */
    String id();
}