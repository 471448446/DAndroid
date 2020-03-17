package com.better.learn.gl20.training.texture.square;

import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlActivity;

/**
 * Created by better on 2020/3/17 10:12 PM.
 */
public class SquareTextureActivity extends GlActivity {

    @Override
    protected GLSurfaceView.Renderer createRender() {
        return new SquareTextureRender(this);
    }

}
