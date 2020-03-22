package com.better.learn.gl20.training.texture.square3;

import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlActivity;

/**
 * Created by better on 2020/3/17 10:12 PM.
 */
public class SquareTexture3Activity extends GlActivity {

    @Override
    protected GLSurfaceView.Renderer createRender() {
        return new SquareTexture3Render(this);
    }
}
