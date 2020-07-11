package com.better.learn.launchmode_flag_new_task;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by better on 2020/6/6 10:19 PM.
 */
public abstract class BaseAct extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Better", "onCreate()" + this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Better", "onDestroy()" + this.getClass().getSimpleName());
    }
}
