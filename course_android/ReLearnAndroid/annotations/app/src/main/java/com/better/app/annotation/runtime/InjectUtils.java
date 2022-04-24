package com.better.app.annotation.runtime;


import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

public class InjectUtils {
    /**
     * 处理所有的注解
     *
     * @param activity 目标页面
     */
    public static void init(Activity activity) {
        Field[] declaredFields = activity.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(InjectView.class)) {
                continue;
            }
            InjectView annotation = declaredField.getAnnotation(InjectView.class);
            if (null == annotation) {
                continue;
            }
            int viewId = annotation.value();
            View viewById = activity.findViewById(viewId);
            try {
                declaredField.setAccessible(true);
                declaredField.set(activity, viewById);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
