package com.better.learn.mobnetinfo;

import android.app.Application;

public class App extends Application {
    public static App shared;

    @Override
    public void onCreate() {
        super.onCreate();
        shared = this;
        AusuReceiver.getInstance().register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AusuReceiver.getInstance().unregister(this);
    }
}
