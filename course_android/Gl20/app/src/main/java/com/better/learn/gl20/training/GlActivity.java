package com.better.learn.gl20.training;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by better on 2020/3/15 9:39 PM.
 */
public abstract class GlActivity extends AppCompatActivity {

    protected GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        // es 版本
        glSurfaceView.setEGLContextClientVersion(2);
        //设置renderer
        glSurfaceView.setRenderer(createRender());
        //设置渲染模式
        /**
         * RENDERMODE_WHEN_DIRTY表示当需要的时候才渲染，只有在调用requestRender或者onResume等方法时才渲染，
         * RENDERMODE_CONTINUOUSLY表示一直渲染。
         * setRenderMode一定要在setRenderer方法之后调用，
         */
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glSurfaceView);
    }

    protected abstract GLSurfaceView.Renderer createRender();

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }
}
