package com.better.app.teststartforegroundservicecrash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Better", "TestService onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Better", "TestService onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return START_NOT_STICKY;
        }
//        startForeground(1024, new NotificationCompat.Builder(getApplicationContext(), CHANNEL)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("test")
//                .setContentText("this is text")
//                .setWhen(System.currentTimeMillis())
//                .build());
        return START_NOT_STICKY;
    }

    public static void start(Context context) {
        Intent service = new Intent(context, TestService.class);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(service);
        } else {
            context.startForegroundService(service);
        }
    }

    private static final String CHANNEL = "test";

    public static void preInit(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(new NotificationChannel(CHANNEL, "test", NotificationManager.IMPORTANCE_HIGH));
    }
}
