package com.better.learn.gl20.training.texture.square2.square;

import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlActivity;

/**
 * Created by better on 2020/3/17 10:12 PM.
 */
public class SquareTexture2Activity extends GlActivity {

    @Override
    protected GLSurfaceView.Renderer createRender() {
        return new SquareTexture2Render(this);
    }

}
