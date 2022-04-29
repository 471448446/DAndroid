package com.better.app.teststartforegroundservicecrash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 检查主线程崩溃后，再次looper，消息是否收到印象
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Better", "________________");
            }
        }, 10_000);

        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TestService.start(getApplicationContext());
//                Looper.getMainLooper().quit();
                throw new RuntimeException("test crash");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        throw new RuntimeException("!!!");
//                    }
//                }).start();
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("!!!thread Crash");
                    }
                }).start();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
    }
}