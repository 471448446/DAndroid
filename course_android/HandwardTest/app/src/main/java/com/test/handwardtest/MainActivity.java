package com.test.handwardtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView helloWorld = (TextView) findViewById(R.id.hello_world);
        StringBuilder sb = new StringBuilder();
        sb.append("x86 = ").append(Utils.isX86Device());
        helloWorld.setText(sb.toString());
    }
}