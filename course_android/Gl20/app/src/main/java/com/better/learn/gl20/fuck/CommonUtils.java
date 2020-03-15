package com.better.learn.gl20.fuck;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

//1,静态导入导入OpenGL ES 2.0常用方法

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    /**
     * 用于读取GLSL Shader文件内容
     *
     * @param context
     * @param resId
     * @return
     */
    public static String readTextFromResource(Context context, int resId) {
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStream = context.getResources().openRawResource(resId);
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                builder.append(nextLine);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    /**
     * 编译着色器
     *
     * @param type
     * @param source
     * @return
     */
    public static int compileShader(int type, String source) {
        final int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            //2,如果着色器创建失败则会返回0
            Log.w(TAG, "Could not create new shader");
            return 0;
        }
        //3,将Shader源文件加载进ID为shaderId的shader中
        glShaderSource(shaderId, source);
        //4,编译这个shader
        glCompileShader(shaderId);
        final int[] status = new int[1];
        //5,获取编译状态储存于status[0]
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0);

        Log.v(TAG, "compile source : \n" + source + "\n" +
                "info log : " + glGetShaderInfoLog(shaderId));
        if (status[0] == 0) {
            //6,检查状态是否正常,0为不正常
            Log.w(TAG, "Compilation of shader failed.");
            return 0;
        }
        return shaderId;
    }

    /**
     * 编译顶点着色器
     *
     * @param source
     * @return
     */
    public static int compileVertexShader(String source) {
        return compileShader(GL_VERTEX_SHADER, source);
    }

    /**
     * 编译片段着色器
     *
     * @param source
     * @return
     */
    public static int compileFragmentShader(String source) {
        return compileShader(GL_FRAGMENT_SHADER, source);
    }

    /**
     * 创建OpenGL对象,并添加着色器,返回OpenGL对象Id
     *
     * @param vertexShaderId
     * @param fragmentShaderId
     * @return
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        //7,创建OpenGL对象
        final int programId = glCreateProgram();
        if (programId == 0) {
            Log.w(TAG, "Create OpenGL program failed");
            return 0;
        }

        //8,在program上附上着色器
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        //9,链接程序
        glLinkProgram(programId);
        final int[] status = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, status, 0);
        Log.v(TAG, "Results of linking program : \n" + glGetProgramInfoLog(programId));
        if (status[0] == 0) {
            Log.w(TAG, "Link program failed");
            return 0;
        }
        return programId;
    }

    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        int i = 0;

        m[i++] = a / aspect;
        m[i++] = 0f;
        m[i++] = 0f;
        m[i++] = 0f;

        m[i++] = 0f;
        m[i++] = a;
        m[i++] = 0f;
        m[i++] = 0f;

        m[i++] = 0f;
        m[i++] = 0f;
        m[i++] = -((f + n) / (f - n));
        m[i++] = -1f;

        m[i++] = 0f;
        m[i++] = 0f;
        m[i++] = -(2f * f * n) / (f - n);
        m[i++] = 0f;
    }

    public static int loadTexture(Context context, int resourceId, boolean isRepeat) {
        /*
         * 第一步 : 创建纹理对象
         */
        final int[] textureObjectId = new int[1];//用于存储返回的纹理对象ID
        GLES20.glGenTextures(1, textureObjectId, 0);
        if (textureObjectId[0] == 0) {//若返回为0,,则创建失败
            Log.w(TAG, "Could not generate a new Opengl texture object");
            return 0;
        }
        /*
         * 第二步: 加载位图数据并与纹理绑定
         */
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;//Opengl需要非压缩形式的原始数据
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            Log.w(TAG, "ResourceId:" + resourceId + "could not be decoded");
            GLES20.glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectId[0]);//通过纹理ID进行绑定

        if (isRepeat) {
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        }

        /*
         * 第三步: 设置纹理过滤
         */
        //设置缩小时为三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //设置放大时为双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        /*
         * 第四步: 加载纹理到Opengl并返回ID
         */
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        return textureObjectId[0];
    }
}
