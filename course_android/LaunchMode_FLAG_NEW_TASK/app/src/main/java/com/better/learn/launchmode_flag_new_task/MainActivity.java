package com.better.learn.launchmode_flag_new_task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动后返回，并没有singleTask 效果,因为是启动的standard
                startActivity(new Intent(MainActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        findViewById(R.id.main_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OtherActivity.class));
            }
        });
    }
}
