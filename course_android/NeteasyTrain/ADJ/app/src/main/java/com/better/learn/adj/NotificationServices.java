package com.better.learn.adj;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by better on 2019/4/14 18:09.
 */
public class NotificationServices extends Service {

    public static final int ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //小于4.3,直接展示一个空通知
            startForeground(ID, new Notification());
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            //7.0之前，空的通知系统的下拉栏会展示某某APP，正在运行，这样用户就看见了。
            // 解决方式是，启动内部类，开启相同的ID通知，然后取消，关闭内部类服务，因为ID是一样的，
            // 所以系统任务改通知取消了，就不会提示用户XX正在运行
            startForeground(ID, new Notification());
            //取消系统的提示 XXApp正在运行
            startService(new Intent(getApplicationContext(), InnerClass.class));
        } else {
            NotificationManager manager = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            String channelS = "channel";
            NotificationChannel channel = new NotificationChannel("channelS", "", NotificationManager.IMPORTANCE_MIN);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                startForeground(ID, new NotificationCompat
                        .Builder(getApplicationContext(), channelS)
                        .build());
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static class InnerClass extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
