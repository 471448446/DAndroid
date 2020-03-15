package com.better.learn.gl20.training.square;

import android.opengl.GLES20;

import com.better.learn.gl20.training.one.GlOneRender;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f}; // top right

    private short index[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

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

    private int vertexStride = COORDS_PER_VERTEX * 4;
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                index.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);

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
        //
        // get handle to vertex shader's vPosition member
        /**
         * 顶点着色器中的坐标变量
         */
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // get handle to fragment shader's vColor member
        /**
         * 片源着色器中的颜色变量
         */
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

    }

    void draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);


        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // 传输定点数据
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
