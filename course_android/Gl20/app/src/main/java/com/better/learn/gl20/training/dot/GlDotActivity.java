package com.better.learn.gl20.training.dot;

import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlActivity;

/**
 * Created by better on 2020/3/15 10:11 PM.
 */
public class GlDotActivity extends GlActivity {
    @Override
    protected GLSurfaceView.Renderer createRender() {
        return new GlDotRander(this);
    }
}
