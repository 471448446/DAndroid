package com.better.learn.gl20.training.texture.square;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.better.learn.gl20.R;
import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 最后展示的是白色：
 * 需要注意的是，在某些硬件上，贴图需要的图片尺寸必须是2的n次方（1,2,4,8,16,32…）。如果你的图片是30X30的话，而且硬件不支持的话，那么你只能看到一个白色的方框（除非，你更改了默认颜色
 * https://yq.aliyun.com/articles/258112
 * Created by better on 2020/3/15 10:22 PM.
 */
public class SquareTextureRender implements GLSurfaceView.Renderer {

    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";//纹理坐标
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";//纹理图

    private Context context;

    private FloatBuffer vertexBuffer;
    private ShortBuffer vertexIndexBuffer;
    private FloatBuffer textureCoordsBuffer;

    // 顶点数据 圆点
    static float coordsVertex[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f}; // top right
    // order to draw vertices
    private short[] indexVertex = {0, 1, 2, 0, 2, 3};
    //纹理坐标的原点是左上角，右下角是（1，1）
    private float[] coordsTexture = {
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f
    };

    // gl相关
    private int program;
    private int aPositionLocation;
    private int uTextureUnitLoc;
    private int aTextureCoordinates;
    private int textureId;

    private final String vertexShaderCode =
            "attribute vec4 a_Position;" +
                    "attribute vec2 a_TextureCoordinates;" +
                    "varying vec2 v_TextureCoordinates;" +
                    "void main() {" +
                    "  v_TextureCoordinates = a_TextureCoordinates;" +
                    "  gl_Position = a_Position;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D u_TextureUnit;" +
                    "varying vec2 v_TextureCoordinates;" + //v_TexCoord：​Vertex Shader传递过来的纹理顶点数据，texture2D是OpenGL ES内置函数，称之为采样器，获取纹理上指定位置的颜色值。
                    "void main() {" +
                    "  gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);" +
                    "}";

    public SquareTextureRender(Context context) {
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
        textureCoordsBuffer = GlUtils.arrayToFloatBuffer(coordsTexture);

        program = GlUtils.createProgram(vertexShaderCode, fragmentShaderCode);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinates = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        uTextureUnitLoc = GLES20.glGetAttribLocation(program, U_TEXTURE_UNIT);
        Bitmap textureBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ss);
        textureId = GlUtils.createTextureId(textureBitmap);
    }

    /**
     * 当Surface发生变化的时候回调，比如竖屏转横屏导致GLSurfaceView大小发生变化，
     * 通常情况下在此方法中设置绘制窗口及和GLSurfaceView大小有关系的参数。
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 执行OpenGL ES渲染工作，由系统以一定的频率来调用重绘View，
     * 当设置GLSurfaceView的渲染模式为GLSurfaceView.RENDERMODE_CONTINUOUSLY或不设置时，系统就会主动回调onDrawFrame()方法,
     * 如果设置为 RENDERMODE_WHEN_DIRTY ，手动调用requestRender()，才会渲染
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);

//        gl.glEnable(GL10.GL_TEXTURE_2D);

        /*
        设置顶点坐标
         */
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        GLES20.glVertexAttribPointer(aPositionLocation, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        /*
        设置纹理坐标
         */
        GLES20.glEnableVertexAttribArray(aTextureCoordinates);
        GLES20.glVertexAttribPointer(aTextureCoordinates, 2,
                GLES20.GL_FLOAT, false, 0, textureCoordsBuffer);//GLES20.GL_FLOAT 要与顶点对应 是float
        /*
        设置纹理
         */
        //激活纹理，GLES20.GL_TEXTURE0表示激活0号纹理，也可以是GLES20.GL_TEXTURE1、GLES20.GL_TEXTURE2等。
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //将纹理绑定到GL_TEXTURE_2D类型
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //设置纹理，textureLoc是Fragment Shader中纹理句柄，后面的参数0和GLES20.GL_TEXTURE0是对应的，
        // 如果启用GLES20.GL_TEXTURE1，那么使用GLES20.glUniform1i(textureLoc, 1)
        GLES20.glUniform1i(uTextureUnitLoc, 0);

        /*
        mode：绘制方式，GLES20.GL_TRIANGLES表示绘制三角形。
        count：顶点的个数。
        type：索引（indices）数组中的元素类型，注意不是顶点的类型，值必须是GL_UNSIGNED_BYTE或者GL_UNSIGNED_SHORT。
        indices：索引数组。
         */
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexVertex.length, GLES20.GL_UNSIGNED_SHORT, vertexIndexBuffer);

        //禁用顶点数据对象
        GLES20.glDisableVertexAttribArray(aPositionLocation);
        //禁用纹理坐标数据对象
        GLES20.glDisableVertexAttribArray(aTextureCoordinates);
        //解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

    }
}
