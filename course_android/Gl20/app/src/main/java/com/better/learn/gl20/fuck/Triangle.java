package com.better.learn.gl20.fuck;

import android.opengl.GLES20;

import com.better.learn.gl20.training.one.GlOneRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by better on 2020/3/14 9:02 PM.
 */
public class Triangle extends Shape {
    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    /**
     * opengl 中原始的数据点的最大是是1，怪不得这里一般都是小数
     */
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    // 定点着色器
    /**
     * gl_Position OpenGl 中的定义的定点的坐标。
     * vec4是着色器语言中的向量类型的一种，包含了四个浮点数的向量。
     * attribute：一般用于每个顶点各不相同的量，如顶点位置等，
     * precision： 同一组顶点组成的相同的量，如光源位置，一组颜色等
     */
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

    private final int mProgram;
    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Triangle() {
        /**
         * 创建定点信息，加入buffer中
         */
        // initialize vertex byte buffer for shape coordinates
        // 一个 float = 4 字节
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        // 规定重元素开始读
        vertexBuffer.position(0);

        /**
         * 编译着色器
         */
        int vertexShader = GlOneRender.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GlOneRender.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        /**
         * 创建OpenGLEs程序，并将着色器连接 OpenGLES程序
         */
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        /**
         * 添加着色器
         */
        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        /**
         * 顶点着色器中的坐标变量
         */
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        // 传输定点数据
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        /**
         * 片源着色器中的颜色变量
         */
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
