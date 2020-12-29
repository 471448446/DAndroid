package com.better.learn.fridademo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static com.better.learn.fridademo.FridaDemo1Activity.TAG;

/**
 * https://11x256.github.io/Frida-hooking-android-part-2/
 * 1. 调用重载方法
 * 2. 调用没有被调用的方法
 *
 * @author Better
 * @date 2020/12/29 10:55
 */
public class FridaDemo2Activity extends AppCompatActivity {

    private String total = "0x@##";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.main_content)).setText("FridaDemo2");
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

    /**
     * 因为它没有被调用，所以直接hook是不行的
     * Now lets assume that we want to call function secret, it is not being called from the onCreate function,
     * so hooking calls to it would be useless.
     */
    String secret() {
        return total;
    }
}