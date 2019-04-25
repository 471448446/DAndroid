package com.better.learn.interfacecommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.better.learn.interfacecommunication.function.InterfaceManager;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.txt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this,
                        InterfaceManager.getInstance().invoke("hello", String.class), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
