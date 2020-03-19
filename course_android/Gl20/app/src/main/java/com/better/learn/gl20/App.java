package com.better.learn.gl20;

import android.app.Application;

/**
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
