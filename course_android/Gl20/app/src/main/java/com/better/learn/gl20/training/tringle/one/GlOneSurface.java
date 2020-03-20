package com.better.learn.gl20.training.tringle.one;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by better on 2020/3/14 4:24 PM.
 */
public class GlOneSurface extends GLSurfaceView {
    private GlOneRender renderer;

    public GlOneSurface(Context context) {
        super(context);
        init();
    }

    public GlOneSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new GlOneRender();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }

}
