package com.better.learn.launchmode1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        // singleInstance 启动standard Activity 并不会直接放入到singleInstance任务栈中，因为singleInstance是只能放一个Activity。
        // 所以这个Activity 放入是 包名所在的任务栈，点击返回就直接看到主页的。
    }
}
