package com.better.learn.ioc.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerDes {
    // setOnClickListener()
    String setter();

    // OnClickListener
    Class<?> type();

    // onClick()
    String callback();
}
