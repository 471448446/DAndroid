package com.better.learn.gl20.training.three;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLThreeRenderer implements GLSurfaceView.Renderer {
    private Triangle triangle;

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    private float[] rotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // 清除背景颜色
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // 设置视图
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        //设置透视投影
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        // 设置相机位置
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // 应用矩阵
        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // 绘制背景颜色 对应glClearColor 真实的生效
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        /**
         * 旋转
         */
        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(rotationMatrix, 0, angle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        // Draw triangle
        triangle.draw(scratch);
    }

    /**
     * 着色程序包含 OpenGL 着色语言 (GLSL) 代码，必须先对其进行编译，然后才能在 OpenGL ES 环境中使用。要编译此代码，请在您的渲染程序类中创建一个实用程序方法
     */
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}