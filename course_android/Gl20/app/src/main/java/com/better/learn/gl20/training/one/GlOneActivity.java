package com.better.learn.gl20.training.one;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
/**
 * https://developer.android.com/training/graphics/opengl/projection
 */
public class GlOneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlOneSurface surface = new GlOneSurface(this);
        setContentView(surface);
    }
}
