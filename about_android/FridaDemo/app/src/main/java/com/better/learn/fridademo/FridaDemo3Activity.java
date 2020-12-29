package com.better.learn.fridademo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.better.learn.fridademo.FridaDemo1Activity.TAG;

/**
 * https://11x256.github.io/Frida-hooking-android-part-3/
 * 利用 Remote Procedure Call 将JS的函数注册到外部，让python调用。
 * 这样就可以修改调用时机，其实这种方案：我在需要调用的时候在运行loader.py是一样的效果
 * 运行：
 * 1. 运行APK后，打开这个页面
 * 2. 命令行执行进入frida/FridaDemo3目录下，python3 loader.py
 *
 * @author Better
 * @date 2020/12/29 15:13
 */
public class FridaDemo3Activity extends AppCompatActivity {

    private String total = "0x@##";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.main_content)).setText("FridaDemo3");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isFinishing()) {
                    fun(50, 30);
                    Log.d(TAG, fun("_A"));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d(TAG, "shit break");
                        break;
                    }
                }
            }
        }).start();
    }

    private void fun(int a, int b) {
        int sum = a + b;
        Log.d(TAG, a + "+" + b + "=" + sum);
    }

    private String fun(String name) {
        total += name;
        return total.toLowerCase();
    }

    String secret() {
        return total;
    }
}