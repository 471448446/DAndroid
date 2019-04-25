package com.better.learn.ioc.lib;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by better on 2019-04-25 22:59.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ListenerDes(setter = "setOnLongClickListener",type = View.OnLongClickListener.class,callback = "onLongClick")
public @interface OnLongClick {
}
