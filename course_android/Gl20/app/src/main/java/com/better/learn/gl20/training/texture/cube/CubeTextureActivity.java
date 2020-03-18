package com.better.learn.gl20.training.texture.cube;

import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlActivity;

/**
 * Created by better on 2020/3/18 2:06 PM.
 */
public class CubeTextureActivity extends GlActivity {

    @Override
    protected GLSurfaceView.Renderer createRender() {
        return new CubeTextureRender(this);
    }
}
