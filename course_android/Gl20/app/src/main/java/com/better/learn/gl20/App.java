package com.better.learn.gl20;

import android.app.Application;

/**
 *
 * 浮点型向量, float, vec2, vec3, vec4, 包含1，2，3，4个元素的浮点型向量.
 * Created by better on 2020/3/19 7:48 PM.
 */
public class App extends Application {
    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
