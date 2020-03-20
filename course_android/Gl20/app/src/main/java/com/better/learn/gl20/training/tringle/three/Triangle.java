package com.better.learn.gl20.training.tringle.three;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 定义形状：
 * 右手作标系
 * 逆时针方向定义顶点
 * 绘制形状：
 * 1. 顶点着色
 * 2. 片段着色
 * 3. 绘制程序
 */
public class Triangle {

    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    /**
     * 绘制形状 step 1.您需要至少一个顶点着色程序来绘制形状，以及一个片段着色程序来为该形状着色。您还必须对这些着色程序进行编译，然后将它们添加到之后用于绘制形状的 OpenGL ES 程序中。
     */
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private int mProgram;

    // Use to access and set the view transformation
    private int vPMatrixHandle;

    public Triangle() {
        /**
         *  创建作标
         */
//        将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        // initialize vertex byte buffer for shape coordinates
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
        vertexBuffer.position(0);

        /**
         * step2 要绘制形状，您必须编译着色程序代码，将它们添加到 OpenGL ES 程序对象中，然后关联该程序。该操作需要在绘制对象的构造函数中完成，因此只需执行一次
         */
        int vertexShader = GLThreeRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLThreeRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        /**
         * 创建Gl 程序
         */
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertexShader);

        // 将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 连接到着色器程序
        GLES20.glLinkProgram(mProgram);
    }

    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    /**
     * step 3 此时，您可以添加绘制形状的实际调用。使用 OpenGL ES 绘制形状时，您需要指定多个参数，以告知渲染管道您要绘制的形状以及如何进行绘制。由于绘制选项因形状而异，因此最好使您的形状类包含它们自己的绘制逻辑。
     *
     * @param vPMatrix
     */
    public void draw(float[] vPMatrix) {
        /**
         *  绘制
         */
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        //设置绘制三角形的颜色
        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        /**
         * 转换
         */
        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0);

        //绘制三角形
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        //禁止顶点数组的句柄
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }


}

