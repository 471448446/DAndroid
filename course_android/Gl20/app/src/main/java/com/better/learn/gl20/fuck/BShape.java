package com.better.learn.gl20.fuck;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;

/**
 * Created by better on 2020/3/14 6:22 PM.
 */
public abstract class BShape extends Shape {

    static final String TAG = "Shape";
    private Context context;

    static final String A_POSITION = "a_Position";
    static final String U_MATRIX = "u_Matrix";
    static final String A_COLOR = "a_Color";

    private float rotateX, rotateY, rotateZ;
    private int width, height;
    private final float[] projectionMatrix = new float[16];
    private final float[] modeMatrix = new float[16];

    int uMatrixLocation;
    int aPositionLocation;
    int program;

    private final Object mMatrixLock = new Object();

    public BShape(Context context) {
        this.context = context;
    }

    protected abstract int getFragmentResourceId();

    protected abstract int getVertexResourceId();


    protected abstract void drawShape();

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        String vertexShaderSource = CommonUtils.readTextFromResource(context, getVertexResourceId());
        String fragmentShaderSource = CommonUtils.readTextFromResource(context, getFragmentResourceId());
        int vertexShader = CommonUtils.compileVertexShader(vertexShaderSource);
        int fragmentShader = CommonUtils.compileFragmentShader(fragmentShaderSource);

        program = CommonUtils.linkProgram(vertexShader, fragmentShader);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
        GLES20.glViewport(0, 0, width, height);
        initProjectionMatrix();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        glUseProgram(program);
        prepareDraw();
        drawShape();
    }

    public void prepareDraw() {
        synchronized (mMatrixLock) {
            initProjectionMatrix();
            setIdentityM(modeMatrix, 0);
            rotateM(modeMatrix, 0, rotateX, 1f, 0f, 0f);
            rotateM(modeMatrix, 0, rotateY, 0f, 1f, 0f);
            rotateM(modeMatrix, 0, rotateZ, 0f, 0f, 1f);
            float[] temp = new float[16];
            multiplyMM(temp, 0, projectionMatrix, 0, modeMatrix, 0);
            System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
            Log.d(TAG, "x ; " + rotateX + " , y : " + rotateY + ", z : " + rotateZ);
            glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        }
    }

    public void rotate(float x, float y, float z) {
        synchronized (mMatrixLock) {
            rotateX += x;
            rotateY += y;
            rotateZ += z;
        }
    }

    private void initProjectionMatrix() {
        synchronized (mMatrixLock) {
            //构建透视投影矩阵
            CommonUtils.perspectiveM(projectionMatrix, 45, (float) width
                    / (float) height, 1f, 10f);
            //重置模型矩阵
            setIdentityM(modeMatrix, 0);
            //设置相机位置
            setLookAtM(modeMatrix, 0, 0f, 3.2f, 3.2f, 0f, 0f, 0f, 0f, 1f, 0f);
            //将模型矩阵和透视投影矩阵相乘获得新的投影矩阵
            final float[] temp = new float[16];
            multiplyMM(temp, 0, projectionMatrix, 0, modeMatrix, 0);
            System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
        }
    }
}
