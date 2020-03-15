package com.better.learn.gl20.training.two;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by better on 2020/3/14 4:24 PM.
 */
public class GlTwoSurface extends GLSurfaceView {
    private GlTwoRender renderer;

    public GlTwoSurface(Context context) {
        super(context);
        init();
    }

    public GlTwoSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new GlTwoRender();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }

}
