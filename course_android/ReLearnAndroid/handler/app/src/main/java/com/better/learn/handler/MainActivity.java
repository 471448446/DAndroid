package com.better.learn.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
        /**
         * 下面这段代码，主要是研究，当挂起当前主线程时，消息循环还能不能工作。
         * 显然是不能
         * 因为主线程被挂起了，这是使用了wait()或wait(5000)，使得主线程处于WAITING或TIMEOUT_WAITING，不管是哪个状态。
         * 主线程都不是处于RUNNING状态了，运行在主线程的MessageQueue是不能再处理下一个消息的。因为当前的这个消息
         * 还没有处理完成，始终处于等待状态，除非有其它的线程通过notify唤醒了主线程，它一直再等待这个notify。
         * 然后这个时候，如果触摸屏幕，或者点击页面按钮，触摸事件会处理超时，也就ANR了。
         */
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
        /**
         * 下面这块代码，主要想研究了，用wait和notify来实现looper的阻塞，准确说是MessageQueue的阻塞。
         * 他们虽然也能完成阻塞，但是需要借宿锁的机制才能完成，既必须要要先获取锁，之后，才能操作。
         * 既：我要定义一个全局的对象，向外暴露出去，等待其它线程来获取锁。
         * 然后当没有消息的时候，在主线程锁住该对象，然后wait()进行等待。
         * 等到有新消息的时候，在其它线程尝试获取锁，在notify()。
         * 这样就比较繁琐了，而且需要写synchronized(xx)的逻辑。
         */
        final Object lock2 = new Object();
        findViewById(R.id.suspend_main_thread_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2_000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean main_run = new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                        /*
                        这里是想模拟，MessageQueue在添加消息后，会判断，释放需要唤醒当前线程。
                        假设，前面的post()就是插入MessageQueue的逻辑，然后，唤醒当前线程的地方，替换了使用 notify()方法
                         */
                        synchronized (lock2) {
                            lock2.notify();
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "before notify also can run " + main_run, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                synchronized (lock2) {
                    try {
                        lock2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
