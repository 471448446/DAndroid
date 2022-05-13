package com.better.learn.handler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * wait() 方法将主线程挂起，从而暂停了主线程Looper的消息处理。进而可能触发ANR（如何阻塞后，有消息要处理）
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Object lock = new Object();
        findViewById(R.id.suspend_main_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Better", "_________onClick() wait main Thread");
//                notifyLock(lock);
                synchronized (lock) {
                    try {
//                        lock.wait();
                        lock.wait(5_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 这里直接超时5s等待
                    // 也可以在其它线程地方唤醒当前wait
                    Log.e("Better", "_________onClick() wait done");
                }
            }
        });
    }

    private void notifyLock(Object lock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    lock.notify();
                }
                if (!isDestroyed()) {
                    findViewById(R.id.suspend_main_thread).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "notify() mainThread!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
