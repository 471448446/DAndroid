package com.better.learn.ioc.lib;

import android.support.annotation.IdRes;
import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ListenerDes(setter = "setOnClickListener", type = View.OnClickListener.class, callback = "onClick")
public @interface OnClick {
    @IdRes int[] value() default {View.NO_ID};
}
