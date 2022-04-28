package com.better.app.teststartforegroundservicecrash;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.weishu.reflection.Reflection;

public class App extends Application {
    public static App shared;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        shared = this;
        Reflection.unseal(base);
        // 只设置主线程的CrashHandler
//        Thread.currentThread().setUncaughtExceptionHandler(new CrashHandler());
        // 设置线程默认的CrashHandler
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        TestService.preInit(this);
        //尝试拦截 startForeground 异常
//        try {
//            hookActivityThread();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void hookActivityThread() throws Exception {
        Class activityThreadCls = Class.forName("android.app.ActivityThread");
        Field sCurrentActivityThreadFiled = activityThreadCls.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThreadFiled.setAccessible(true);
        Object activityThread = sCurrentActivityThreadFiled.get(null);
        Field mHFiled = activityThreadCls.getDeclaredField("mH");
        mHFiled.setAccessible(true);
        Handler mH = (Handler) mHFiled.get(activityThread);
        Class handlerCls = Class.forName("android.os.Handler");
        Field mCallbackField = handlerCls.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        mCallbackField.set(mH, new HandlerCallback());
    }

    static final class HandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.d("Better", "handleMessage()" + msg.what);
            if (134 == msg.what) {
                if (msg.obj instanceof String) {
                    String reason = (String) msg.obj;
                    Matcher matcher = Pattern
                            .compile("Context.startForegroundService\\(\\) did not then call Service.startForeground\\(\\)(.+)TestService")
                            .matcher(reason);
                    boolean crash = matcher.find();
                    if (crash) {
                        Log.d("Better", reason);
                        Toast.makeText(App.shared, "crash", Toast.LENGTH_SHORT).show();
                    }
                    return crash;
                }
            }
            return false;
        }
    }
}
