package com.better.learn.filelock;

import android.app.Application;

public class App extends Application {
    public static App shared;

    @Override
    public void onCreate() {
        super.onCreate();
        shared = this;
    }
}
