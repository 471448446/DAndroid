package com.better.learn.gl20.fuck;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by better on 2020/3/14 9:07 PM.
 */
public class TestTrinalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        setContentView(glSurfaceView);

        MyRenderer myRenderer = new MyRenderer();
        glSurfaceView.setEGLContextClientVersion(2);
        myRenderer.addShape(new Triangle());

        glSurfaceView.setRenderer(myRenderer);
    }
}
