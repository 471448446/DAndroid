package com.better.learn.gl20.training.cube;

import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlActivity;

/**
 * Created by better on 2020/3/15 9:15 PM.
 */
public class CubeActivity extends GlActivity {

    @Override
    protected GLSurfaceView.Renderer createRender() {
        return new CubeRender();
    }

}
