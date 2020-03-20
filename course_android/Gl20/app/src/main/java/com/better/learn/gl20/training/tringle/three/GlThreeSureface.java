package com.better.learn.gl20.training.tringle.three;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GlThreeSureface extends GLSurfaceView {
    GLThreeRenderer glRender;

    public GlThreeSureface(Context context) {
        super(context);
        init(context);
    }

    public GlThreeSureface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        glRender = new GLThreeRenderer();
        // Create an OpenGL ES 2.0 context ??
        setEGLContextClientVersion(2);
        setRenderer(glRender);
        // 怎么渲染
        // Render the view only when there is a change in the drawing data.
        // To allow the triangle to rotate automatically, this line is commented out:
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
