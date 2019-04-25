package com.better.learn.ioc.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 申明在类上方使用
@Target(ElementType.TYPE)
// 申明在运行的时候才注解
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {
    // 获取注解的值
    int value();
}
