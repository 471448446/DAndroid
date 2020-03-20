package com.better.learn.gl20.training.texture.cube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 直接话颜色，能画出来
 * Created by better on 2020/3/15 10:22 PM.
 */
public class CubeTextureRender4NoTexture implements GLSurfaceView.Renderer {
    /*
     着色器代码
  */
    private static final String vertexShaderCode =
            "uniform mat4 u_MVPMatrix;                                  \n" +
                    "attribute vec4 a_Position;                         \n" +
                    "void main() {                                      \n" +
                    "  gl_Position = u_MVPMatrix * a_Position;          \n" +
                    "}                                                  \n";

    private static final String fragmentShaderCode =
            "precision mediump float;                                                   \n" +
                    "uniform vec4 u_Color;                                              \n" +
                    "void main() {                                                      \n" +
                    "  gl_FragColor = u_Color;                                          \n" +
                    "}                                                                  \n";

    private static final String A_POSITION = "a_Position";
    private static final String U_MVP_MATRIX = "u_MVPMatrix";
    private static final String U_COLOR = "u_Color";

    private Context context;

    private FloatBuffer vertexBuffer;
    private ShortBuffer vertexIndexBuffer;
    private FloatBuffer colorBuffer;

    private float r = 0.5f;
    private float coordsVertex[] = {
            -r, r, r,    //正面左上0
            -r, -r, r,   //正面左下1
            r, -r, r,    //正面右下2
            r, r, r,     //正面右上3
            -r, r, -r,    //反面左上4
            -r, -r, -r,   //反面左下5
            r, -r, -r,    //反面右下6
            r, r, -r,     //反面右上7
    };
    private short[] indexVertex = {
            //前
            0, 1, 2, 0, 2, 3,
            //后
            7, 6, 5, 7, 5, 4,
            //上
            3, 7, 4, 3, 4, 0,
            //底
            1, 5, 6, 1, 6, 2,
            //左
            0, 4, 5, 0, 5, 1,
            //右
            2, 6, 7, 2, 7, 3,
    };
    float color[] = {
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 1f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 0f, 1f
    };


    // gl相关
    private int program;
    private int aPositionLocation;
    private int uMvpMatrixLoc;
    private int uColorLoc;

    // 矩阵
    // 投影
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] rotationMatrix = new float[16];

    private float rotateX, rotateY, rotateZ;

    public void setRotate(float x, float y, float z) {
        this.rotateX += x;
        this.rotateY += y;
        this.rotateZ += z;
    }

    public CubeTextureRender4NoTexture(Context context) {
        this.context = context;
    }

    /**
     * GLSurfaceView创建完成，也代表OpenGL ES环境创建完成，通常情况下在此方法中创建Program及初始化参数。
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0.0f, 0.0f, 1.0f);

        vertexBuffer = GlUtils.arrayToFloatBuffer(coordsVertex);
        vertexIndexBuffer = GlUtils.arrayToShortBuffer(indexVertex);
        colorBuffer = GlUtils.arrayToFloatBuffer(color);

        program = GlUtils.createProgram(vertexShaderCode, fragmentShaderCode);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMvpMatrixLoc = GLES20.glGetUniformLocation(program, U_MVP_MATRIX);
        uColorLoc = GLES20.glGetUniformLocation(program, U_COLOR);

    }

    /**
     * 当Surface发生变化的时候回调，比如竖屏转横屏导致GLSurfaceView大小发生变化，
     * 通常情况下在此方法中设置绘制窗口及和GLSurfaceView大小有关系的参数。
     */
    @Override

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);


        float ratio = (float) width / height;

        //设置透视投影
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // 设置相机位置
        Matrix.setLookAtM(viewMatrix, 0, 2, 2, 4.2f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // 应用矩阵
        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    /**
     * 执行OpenGL ES渲染工作，由系统以一定的频率来调用重绘View，
     * 当设置GLSurfaceView的渲染模式为GLSurfaceView.RENDERMODE_CONTINUOUSLY或不设置时，系统就会主动回调onDrawFrame()方法,
     * 如果设置为 RENDERMODE_WHEN_DIRTY ，手动调用requestRender()，才会渲染
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = prepareDraw();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);


        /*
        设置顶点坐标
         */
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        GLES20.glVertexAttribPointer(aPositionLocation, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(uMvpMatrixLoc, 1, false, scratch, 0);
        GLES20.glUniform4fv(uColorLoc, 1, colorBuffer);

        /*
        mode：绘制方式，GLES20.GL_TRIANGLES表示绘制三角形。
        count：顶点的个数。
        type：索引（indices）数组中的元素类型，注意不是顶点的类型，值必须是GL_UNSIGNED_BYTE或者GL_UNSIGNED_SHORT。
        indices：索引数组。
         */
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexVertex.length, GLES20.GL_UNSIGNED_SHORT, vertexIndexBuffer);

        //禁用顶点数据对象
        GLES20.glDisableVertexAttribArray(aPositionLocation);

    }

    private float[] prepareDraw() {
        Log.e("Better", "onDrawFrame" + rotateX + "," + rotateY + "," + rotateZ);

        float[] scratch = new float[16];

        Matrix.setRotateM(rotationMatrix, 0, rotateX, 1f, 0, 0f);
        Matrix.setRotateM(rotationMatrix, 0, rotateY, 0, 1f, 0f);
        Matrix.setRotateM(rotationMatrix, 0, rotateZ, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        return scratch;
    }

}
