package com.better.learn.fridademo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * https://11x256.github.io/Frida-hooking-android-part-1/
 * 对应执行文件/frdia/FridaDemo1/*
 * 1. 拦截函数
 * 2. 打印日志
 * 3. 修改参数
 * 这种方式打印参数日志，比起修改APK看日志（反编译APK修改smali代码再打包）方便多了
 * @author Better
 * @date 2020/12/28 10:14
 */
public class FridaDemo1Activity extends AppCompatActivity {
    public static final String TAG = "Frida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.main_content)).setText("FridaDemo1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isFinishing()) {
                    fun(50, 30);
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
}