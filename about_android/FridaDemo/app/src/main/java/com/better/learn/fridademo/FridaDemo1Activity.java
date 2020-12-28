package com.better.learn.fridademo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

/**
 * https://11x256.github.io/Frida-hooking-android-part-1/
 * 对应执行文件/frdia/FridaDemo1/*
 *
 * @author Better
 * @date 2020/12/28 10:14
 */
public class FridaDemo1Activity extends AppCompatActivity {
    public static final String TAG = "Frida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, "shit break");
                break;
            }
            fun(50, 30);
        }
    }

    void fun(int a, int b) {
        int sum = a + b;
        Log.d(TAG, a + "+" + b + "=" + sum);
    }
}