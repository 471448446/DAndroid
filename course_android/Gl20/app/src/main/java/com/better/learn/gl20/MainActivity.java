package com.better.learn.gl20;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * https://developer.android.com/training/graphics/opengl/projection
 */
public class MainActivity extends AppCompatActivity {

    private MyGlSureface glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        glSurfaceView = new MyGlSureface(this);
        setContentView(glSurfaceView);
    }

}
