package com.better.learn.interfacecommunication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.better.learn.interfacecommunication.function.FunctionHasResultNoParm;
import com.better.learn.interfacecommunication.function.InterfaceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InterfaceManager.getInstance().addFunction(new FunctionHasResultNoParm<String>("hello") {
            @Override
            public String function() {
                return "Hello";
            }
        });
        findViewById(R.id.txt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
}
