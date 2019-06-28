package com.better.learn.androidx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * https://developer.android.com/jetpack/androidx
 * https://developer.android.com/jetpack/androidx/releases/archive/androidx#1.0.0a1-ki
 * https://developer.android.com/jetpack/androidx/migrate
 * <p>
 * 修改Project的gradle.properties 不要修改全局的gradle.properties:
 * android.useAndroidX=true
 * android.enableJetifier=true
 * Refactor-> migrate to AndroidX
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
