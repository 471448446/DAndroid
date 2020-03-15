package com.better.learn.gl20.fuck;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

public class MyRenderer implements GLSurfaceView.Renderer {

    private List<Shape> shapes = new CopyOnWriteArrayList<Shape>();

    public void addShape(Shape program) {
        shapes.add(program);
    }

    public void clearShape() {
        shapes.clear();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        for (Shape program : shapes) {
            program.onSurfaceCreated(gl, config);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        for (Shape program : shapes) {
            program.onSurfaceChanged(gl, width, height);
        }
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        for (Shape program : shapes) {
            program.onDrawFrame(gl);
        }
    }

}
