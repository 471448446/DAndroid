package com.better.learn.filelock;

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

import java.io.File;
import java.io.IOException;

public class ProcessService1 extends Service {
    public static File getTargetFile() {
        File file = new File(App.shared.getExternalFilesDir("lock"), "s1.cc");
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private LockFile lockFile;

    @Override
    public void onCreate() {
        super.onCreate();
        lockFile = new LockFileImpl(getTargetFile());
        String channel = "sp";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel, "s1", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        startForeground(1001,
                new NotificationCompat.Builder(this, channel)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("f1")
                        .setContentText("this is process s1")
                        .setTicker("s1")
                        .setWhen(System.currentTimeMillis())
                        .build()
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lockFile.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return Service.START_NOT_STICKY;
        }
        if (intent.getBooleanExtra("stop", false)) {
            stopSelf();
            return START_NOT_STICKY;
        }
        Log.d("Better", "ProcessService start");
        if (intent.hasExtra(MainActivity.LOCK_FILE_ENABLE)) {
            int intExtra = intent.getIntExtra(MainActivity.LOCK_FILE_ENABLE, 0);
            Log.d("Better", "ProcessService receive lock: " + intExtra);
            switch (intExtra) {
                case MainActivity.LOCK:
                    Log.d("Better", "lock file: " + lockFile.lock());
                    break;
                case MainActivity.LOCK_NB:
                    Log.d("Better", "try lock file: " + lockFile.tryLock());
                    break;
                case MainActivity.LOCK_UN:
                    lockFile.unLock();
                    Log.d("Better", "release lock file");
                    break;
            }
        }
        return Service.START_NOT_STICKY;
    }
}
