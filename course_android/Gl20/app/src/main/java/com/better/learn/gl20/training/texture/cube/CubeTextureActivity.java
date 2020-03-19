package com.better.learn.gl20.training.texture.cube;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.better.learn.gl20.training.GlActivity;

import androidx.annotation.Nullable;

/**
 * Created by better on 2020/3/18 2:06 PM.
 */
public class CubeTextureActivity extends GlActivity {
    private CubeTextureRender mCubeTextureRender;

    private float downX, downY;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mCubeTextureRender = new CubeTextureRender(this);
        super.onCreate(savedInstanceState);
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCubeTextureRender.setRoateX((event.getX() - downX) / 10);
                        mCubeTextureRender.setRoateX((event.getX() - downY) / 10);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected GLSurfaceView.Renderer createRender() {
        return mCubeTextureRender;
    }
}
