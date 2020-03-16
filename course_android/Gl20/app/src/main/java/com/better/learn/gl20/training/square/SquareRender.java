package com.better.learn.gl20.training.square;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by better on 2020/3/15 10:22 PM.
 */
public class SquareRender implements GLSurfaceView.Renderer {

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer colorBuffer;

    // 顶点数据 圆点
    static float coords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f}; // top right
    // order to draw vertices
    private short[] index = {0, 1, 2, 0, 2, 3};
    //color
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    // gl相关
    private int program;
    private int vPositionLoc;
    private int uColorLoc;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public SquareRender() {

    }

    /**
     * GLSurfaceView创建完成，也代表OpenGL ES环境创建完成，通常情况下在此方法中创建Program及初始化参数。
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0.0f, 0.0f, 1.0f);

        vertexBuffer = GlUtils.arrayToFloatBuffer(coords);
        indexBuffer = GlUtils.arrayToShortBuffer(index);
        //  r,g,b,a
        colorBuffer = GlUtils.arrayToFloatBuffer(color);

        program = GlUtils.createProgram(vertexShaderCode, fragmentShaderCode);
        vPositionLoc = GLES20.glGetAttribLocation(program, "vPosition");
        uColorLoc = GLES20.glGetUniformLocation(program, "vColor");
    }

    /**
     * 当Surface发生变化的时候回调，比如竖屏转横屏导致GLSurfaceView大小发生变化，
     * 通常情况下在此方法中设置绘制窗口及和GLSurfaceView大小有关系的参数。
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 执行OpenGL ES渲染工作，由系统以一定的频率来调用重绘View，
     * 当设置GLSurfaceView的渲染模式为GLSurfaceView.RENDERMODE_CONTINUOUSLY或不设置时，系统就会主动回调onDrawFrame()方法,
     * 如果设置为 RENDERMODE_WHEN_DIRTY ，手动调用requestRender()，才会渲染
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(vPositionLoc);
        GLES20.glVertexAttribPointer(vPositionLoc, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(uColorLoc, 1, colorBuffer);
//        GLES20.glUniform4fv(uColorLoc, 1, color, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vPositionLoc);
    }
}
