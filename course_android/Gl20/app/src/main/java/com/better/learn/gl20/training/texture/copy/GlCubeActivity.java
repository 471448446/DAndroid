package com.better.learn.gl20.training.texture.copy;

import android.annotation.SuppressLint;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.better.learn.gl20.training.GlActivity;

import androidx.annotation.Nullable;

/**
 * Created by better on 2020/3/20 8:37 PM.
 */
public class GlCubeActivity extends GlActivity {
    private CubeRotateRender render;

    private int lastX, lastY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        render = new CubeRotateRender();
        super.onCreate(savedInstanceState);
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getX();
                        lastY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        int dx = x - lastX;
                        int dy = y - lastY;
                        lastX = x;
                        lastY = y;
                        render.setRotate(dy / 10f, dx / 10f, 0f);
                        glSurfaceView.requestRender();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected GLSurfaceView.Renderer createRender() {
        return render;
    }
}
