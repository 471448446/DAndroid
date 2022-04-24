package com.better.app.annotation.runtime;

import android.app.Activity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 演示直接通过反射处理注解
 * 因为是反射，是代码运行的时候，处理，所以是运行时
 * 正是因为使用了反射，所以会有性能问题
 * 比如{@link InjectUtils#init(Activity)}中要挨个去遍历所有的属性，判断是没使用某个注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectView {
    /**
     * @return view 的 id
     */
    int value();
}
