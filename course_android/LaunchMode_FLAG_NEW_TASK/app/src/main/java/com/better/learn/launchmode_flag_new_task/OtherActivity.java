package com.better.learn.launchmode_flag_new_task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OtherActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        findViewById(R.id.main_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtherActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        findViewById(R.id.main_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这种方式 会清除之前的实例到栈顶的Activity，在创建一个新的放入到栈中 并不会调用`onNewIntent()`
                startActivity(new Intent(OtherActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        findViewById(R.id.main_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这种方式 像singleTask，启动主页后，是将之前的主页展现，并移除Activity
                startActivity(new Intent(OtherActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
}