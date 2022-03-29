package com.better.app.intentservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello_world).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("task_id", "task01");
                startService(intent);

                Intent intent2 = new Intent(MainActivity.this, MyService.class);
                intent2.putExtra("task_id", "task02");
                startService(intent2);
            }
        });
    }
}