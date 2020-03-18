package com.better.learn.gl20.training;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * https://blog.csdn.net/cassiePython/article/details/51635744
 * https://www.jianshu.com/p/b3455af356bb
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

    public static int createTextureId(Bitmap bitmap) {
        /*
         * 第一步 : 创建纹理对象
         */
        int[] textures = new int[1];
        /*
        -count：生成纹理的个数。
        -array：生成纹理id存放的数组。
        -offset：存放纹理id数组的偏移。
         */
        GLES20.glGenTextures(1, textures, 0);
        if (textures[0] == 0) {//若返回为0,,则创建失败
            return 0;
        }
        // 通过纹理ID进行绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        /*
         * 第三步: 设置纹理过滤
         */
        //设置缩小时为三线性过滤
        /*
            target:活动纹理单元的目标纹理，GLES20.GL_TEXTURE_2D表示2D纹理，还有其他纹理，比如GLES11Ext.GL_TEXTURE_EXTERNAL_OES，
                    这是Android特有的OES纹理，预览相机或者视频使用此纹理
            pname: 纹理参数的标记名，可以设置的值如下：GL_TEXTURE_MIN_FILTER, GL_TEXTURE_MAG_FILTER, GL_TEXTURE_WRAP_S 或 GL_TEXTURE_WRAP_T
            param: 对应的值
            https://www.jianshu.com/p/5e1c99e4c1b4
         */
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
        );
        //设置放大时为双线性过滤
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
        );
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT
        );
        GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT
        );
        /*
         * 第四步: 加载纹理到Opengl并返回ID
         */
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        //绑定bitmap 后生成Mipmap
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        return textures[0];
    }


}
