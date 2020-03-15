package com.better.learn.gl20.training.two;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GlTwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlTwoSurface surface = new GlTwoSurface(this);
        setContentView(surface);
    }
}
