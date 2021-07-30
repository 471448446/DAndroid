package com.better.learn.filelock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/***
 * 尝试获取另外一个进程已经获取文件锁
 * 阻塞当前线程，因为直接运行在主线程，所以会阻塞主线程
 */
public class MainActivity extends AppCompatActivity {
    public static final String LOCK_FILE_ENABLE = "LOCK_FILE_ENABLE";
    /**
     * 阻塞方式
     */
    public static final int LOCK = 1;
    /**
     * 非阻塞方式
     */
    public static final int LOCK_NB = 2;
    /**
     * 释放锁
     */
    public static final int LOCK_UN = 3;

    LockFile lockFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lockFile = new LockFileImpl(ProcessService1.getTargetFile());
        findViewById(R.id.process_s1_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ProcessService1.class));
            }
        });
        findViewById(R.id.process_s1_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ProcessService1.class)
                        .putExtra("stop", true));
            }
        });
        findViewById(R.id.process1_tryLock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ProcessService1.class)
                        .putExtra(LOCK_FILE_ENABLE, LOCK_NB));
            }
        });
        findViewById(R.id.process1_lock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ProcessService1.class)
                        .putExtra(LOCK_FILE_ENABLE, LOCK));
            }
        });
        findViewById(R.id.process1_unLock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, ProcessService1.class)
                        .putExtra(LOCK_FILE_ENABLE, LOCK_UN));
            }
        });
        findViewById(R.id.main_lock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Better", "lock file: " + lockFile.lock());
            }
        });
        findViewById(R.id.main_tryLock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Better", "try lock file: " + lockFile.tryLock());
            }
        });
        findViewById(R.id.main_unLock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockFile.unLock();
                Log.d("Better", "un lock file: ");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lockFile.onDestroy();
    }
}