package com.better.learn.gl20.training.texture.copy;

import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlActivity;

/**
 * Created by better on 2020/3/20 8:37 PM.
 */
public class GlCubeActivity extends GlActivity {
    @Override
    protected GLSurfaceView.Renderer createRender() {
        return new Cube2Render();
    }
}
