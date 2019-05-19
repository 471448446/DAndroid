package com.better.learn.library;

import android.app.Activity;

/**
 * Created by better on 2019-05-19 10:46.
 */
public class ButterKnife {
    public static void bind(Activity activity) {
        // 使用生成的Binding文件来完成注解
        // 获取类信息路径以及名称
        String className = activity.getClass().getName() + "_ViewBinder";
        try {
            Class<?> binderClass = Class.forName(className);
            ViewBinder binder = (ViewBinder) binderClass.newInstance();
            binder.bind(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
