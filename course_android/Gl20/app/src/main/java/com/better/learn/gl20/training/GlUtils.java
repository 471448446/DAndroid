package com.better.learn.gl20.training;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by better on 2020/3/15 9:52 PM.
 */
public class GlUtils {
    public static ShortBuffer arrayToShortBuffer(short[] index) {
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                index.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);
        return indexBuffer;
    }

    public static FloatBuffer arrayToFloatBuffer(float[] coords) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    public static int createProgram(Context context, int rawVertex, int fragment) {
        return createProgram(ResourceUtils.readTextFromResource(context, rawVertex),
                ResourceUtils.readTextFromResource(context, fragment));
    }

    public static int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        /**
         * 编译着色器
         */
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        /**
         * 创建OpenGLEs程序，并将着色器连接 OpenGLES程序
         */
        // create empty OpenGL ES Program
        int program = GLES20.glCreateProgram();

        /**
         * 添加着色器
         */
        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program);

        return program;
    }

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
