package com.better.app.plugin1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView main = findViewById(R.id.main_txt);
        main.setText("Hello plugin1!\n MainActivityJava}");
    }
}