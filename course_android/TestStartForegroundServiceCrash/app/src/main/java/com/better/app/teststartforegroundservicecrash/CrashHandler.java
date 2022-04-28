package com.better.app.teststartforegroundservicecrash;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.d("Better", "crash begin-------------------");
        e.printStackTrace();
        Log.d("Better", e.getMessage());
        Log.d("Better", "crash end-------------------");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Better", "check if messageQueue running");
            }
        }, 100);
        // 收到一个UI线程的crash
        // 此处，如果不做任何处理会ANR，猜测是Looper退出循环了
        // 下面尝试开启循环
//        Looper mainLooper = Looper.getMainLooper();
//        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
//        rescue();

//        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());

        /**
         第一次崩溃，可以正常处理，后续如果在来一次崩溃就ANR了
         第一次崩溃时，主线程在处理这个方法uncaughtException，按照逻辑主线程应该在uncaughtException调用完成后，主线程退出。
         但是，下面{@link #looper()}这里死循了，让主线程不会结束。虽然这里崩溃了，
         但是这里死循环，让主线程退不出去 {@link ActivityThread#main} 那么main方法里面的looper肯定也是不能继续执行的
         */
        looper();
    }

    private void rescue() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Better", "try rescue main!!!!!!!!!!");
                try {
                    Class<Looper> looperClass = Looper.class;
                    Field sThreadLocalField = looperClass.getDeclaredField("sThreadLocal");
                    sThreadLocalField.setAccessible(true);
                    ThreadLocal<Looper> sThreadLocal = (ThreadLocal<Looper>) sThreadLocalField.get(null);
                    sThreadLocal.set(Looper.getMainLooper());
                    looper();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Better", "fail rescue !!!!!!!!!!");
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private void looper() {
        Log.d("Better", Looper.myLooper() + "");
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("Better", " getMainLooper() not died-------------------");
            }
        });
        //这里运行在主线程，所以获取的是主线程的Looper
        Looper.loop();
        Log.d("Better", " loop() died-------------------");
    }
}
