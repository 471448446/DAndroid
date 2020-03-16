package com.better.learn.gl20.training.cube;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * https://www.jianshu.com/p/b7fd566ab450
 * https://blog.csdn.net/cassiePython/article/details/51589634
 * Created by better on 2020/3/15 10:22 PM.
 */
public class CubeRender implements GLSurfaceView.Renderer {

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer colorBuffer;

    // 顶点数据 圆点
    private float r = 0.5f;

    private float coords[] = {
            -r, r, r,//0
            -r, -r, r,//1
            r, -r, r,//2
            r, r, r,//3
            r, -r, -r,//4
            r, r, -r,//5
            -r, -r, -r,//6
            -r, r, -r//7
    };
    // order to draw vertices
    private short[] index = {
            0, 1, 2, 0, 2, 3,
            3, 2, 4, 3, 4, 5,
            5, 4, 6, 5, 6, 7,
            7, 6, 1, 7, 1, 0,
            7, 0, 3, 7, 3, 5,
            6, 1, 2, 6, 2, 4
    };
    //color
    float color[] = {
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f
    };

    // gl相关
    private int program;
    private int vPositionLoc;
    private int mvpMatrixLoc;
    private int uColorLoc;

    private final String vertexShaderCode =
            "attribute vec4 aPosition;" +
                    "uniform mat4 mvpMatrix;" +
                    "void main() {" +
                    "  gl_Position = mvpMatrix * aPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 uColor;" +
                    "void main() {" +
                    "  gl_FragColor = uColor;" +
                    "}";

    //矩阵
    float[] mProjMatrix = new float[16];// 4x4矩阵 存储投影矩阵
    float[] mVMatrix = new float[16];// 摄像机位置朝向9参数矩阵
    float[] mMVPMatrix = new float[16];

    public CubeRender() {

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
        vPositionLoc = GLES20.glGetAttribLocation(program, "aPosition");
        mvpMatrixLoc = GLES20.glGetUniformLocation(program, "mvpMatrix");
        uColorLoc = GLES20.glGetUniformLocation(program, "uColor");
    }

    /**
     * 当Surface发生变化的时候回调，比如竖屏转横屏导致GLSurfaceView大小发生变化，
     * 通常情况下在此方法中设置绘制窗口及和GLSurfaceView大小有关系的参数。
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 20, 100);
        Matrix.setLookAtM(mVMatrix, 0, -16f, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    /**
     * 执行OpenGL ES渲染工作，由系统以一定的频率来调用重绘View，
     * 当设置GLSurfaceView的渲染模式为GLSurfaceView.RENDERMODE_CONTINUOUSLY或不设置时，系统就会主动回调onDrawFrame()方法,
     * 如果设置为 RENDERMODE_WHEN_DIRTY ，手动调用requestRender()，才会渲染
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        /*
        // 开启深度检测 无效？？
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
         */

        GLES20.glUseProgram(program);
        //传入顶点
        GLES20.glEnableVertexAttribArray(vPositionLoc);
        GLES20.glVertexAttribPointer(vPositionLoc, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        // 应用矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        /*GLES20.glUniform4f(uColorLoc, 1.0f, 0.0f, 0.0f, 0.0f);*/ //颜色写死
        GLES20.glUniform4fv(uColorLoc, 1, colorBuffer);
        GLES20.glUniformMatrix4fv(mvpMatrixLoc, 1, false, mMVPMatrix, 0);
        // 绘图
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vPositionLoc);

    }
}
