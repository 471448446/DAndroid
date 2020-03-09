package com.better.learn.gl20;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MyGlSureface extends GLSurfaceView {
    MyGLRenderer glRender;

    public MyGlSureface(Context context) {
        super(context);
        init(context);
    }

    public MyGlSureface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        glRender = new MyGLRenderer();
        // Create an OpenGL ES 2.0 context ??
        setEGLContextClientVersion(2);
        setRenderer(glRender);
        // 怎么渲染
        // Render the view only when there is a change in the drawing data.
        // To allow the triangle to rotate automatically, this line is commented out:
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
