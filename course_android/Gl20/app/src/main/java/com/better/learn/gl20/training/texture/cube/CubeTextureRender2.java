package com.better.learn.gl20.training.texture.cube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.better.learn.gl20.R;
import com.better.learn.gl20.fuck.CommonUtils;
import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;

/**
 * 最后渲染不对。文理坐标不对
 * Created by better on 2020/3/15 10:22 PM.
 */
public class CubeTextureRender2 implements GLSurfaceView.Renderer {
    /*
     着色器代码
  */
    private static final String vertexShaderCode =
            "uniform mat4 u_MVPMatrix;                                  \n" +
                    "attribute vec4 a_Position;                         \n" +
                    "attribute vec2 a_TextureCoordinates;               \n" +
                    "varying vec2 v_TextureCoordinates;                 \n" +
                    "void main() {                                      \n" +
                    "  v_TextureCoordinates = a_TextureCoordinates;     \n" +
                    "  gl_Position = u_MVPMatrix * a_Position;          \n" +
                    "}                                                  \n";

    private static final String fragmentShaderCode =
            "precision mediump float;                                                   \n" +
                    "uniform sampler2D u_TextureUnit;                                   \n" +
                    "varying vec2 v_TextureCoordinates;                                 \n" + //v_TexCoord：​Vertex Shader传递过来的纹理顶点数据，texture2D是OpenGL ES内置函数，称之为采样器，获取纹理上指定位置的颜色值。
                    "void main() {                                                      \n" +
                    "  gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);   \n" +
                    "}                                                                  \n";

    private static final String A_POSITION = "a_Position";
    private static final String U_MVP_MATRIX = "u_MVPMatrix";
    private static final String U_COLOR = "u_Color";
    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";//纹理坐标
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";//纹理图

    private Context context;

    private FloatBuffer vertexBuffer;
    private ShortBuffer vertexIndexBuffer;
    private FloatBuffer textureCoordsBuffer;

    // 顶点数据 圆点 https://blog.csdn.net/zhangjikuan/article/details/23126639
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

    //纹理坐标的原点是左上角，右下角是（1，1）
    private float[] coordsTexture = {
            0, 0, 0, 1, 1, 1, 1, 0, //前面
            0, 1, 1, 1, 1, 0, 0, 0, //上面？ 后？
            0, 1, 0, 0, 1, 0, 1, 1, //后面？ 上？ 
            0, 0, 0, 1, 1, 1, 1, 0, //下面  
            1, 0, 0, 0, 0, 1, 1, 1, //左面
            0, 1, 1, 1, 0, 0, 1, 0, //右面
    };

    /*
      0, 0, 0, 1, 1, 1, 1, 0, //前面
            0, 1, 0, 0, 1, 0, 1, 1, //后面 
            1, 1, 1, 0, 0, 0, 1, 0, //上面   
            0, 1, 0, 0, 1, 0, 1, 1, //下面  
            0, 1, 1, 1, 1, 0, 0, 0, //左面
            1, 1, 1, 0, 0, 0, 0, 1, //右面
     */
    /*
     左右错误
         0, 0, 0, 1, 1, 1, 1, 0, //前面
            0, 1, 0, 0, 1, 0, 1, 1, //后面 
            0, 1, 1, 1, 1, 0, 0, 0, //上面   
            0, 0, 0, 1, 1, 1, 1, 0, //下面  
            0, 1, 1, 1, 1, 0, 0, 0, //左面
            1, 1, 1, 0, 0, 0, 0, 1, //右面
     */

    /*
           0, 1, 1, 1, 1, 0, 0, 0, //前面
            0, 1, 0, 0, 1, 0, 1, 1, //后面 
            0, 1, 1, 1, 1, 0, 0, 0, //上面   
            0, 0, 0, 1, 1, 1, 1, 0, //下面  
            0, 1, 1, 1, 1, 0, 0, 0, //左面
            1, 1, 1, 0, 0, 0, 0, 1, //右面
     */

    // gl相关
    private int program;
    private int aPositionLocation;
    private int uMvpMatrixLoc;
    private int uTextureUnitLoc;
    //    private int uColorLoc;
    private int aTextureCoordinates;
    private int textureId;

    // 矩阵
    // 投影
    private final float[] projectionMatrix = new float[16];
    private final float[] modeMatrix = new float[16];

    private float rotateX, rotateY, rotateZ;
    private float ratio;

    public void setRotate(float x, float y, float z) {
        this.rotateX += x;
        this.rotateY += y;
        this.rotateZ += z;
    }

    public CubeTextureRender2(Context context) {
        this.context = context;
    }

    /**
     * GLSurfaceView创建完成，也代表OpenGL ES环境创建完成，通常情况下在此方法中创建Program及初始化参数。
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0.0f, 0.0f, 1.0f);
        //开启深度测试,效果是让OpenGLES知道那个面在前那个面在后(存储Z轴)，
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        vertexBuffer = GlUtils.arrayToFloatBuffer(coordsVertex);
        vertexIndexBuffer = GlUtils.arrayToShortBuffer(indexVertex);
        textureCoordsBuffer = GlUtils.arrayToFloatBuffer(coordsTexture);

        program = GlUtils.createProgram(vertexShaderCode, fragmentShaderCode);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMvpMatrixLoc = GLES20.glGetUniformLocation(program, U_MVP_MATRIX);
        aTextureCoordinates = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        uTextureUnitLoc = GLES20.glGetAttribLocation(program, U_TEXTURE_UNIT);
        Bitmap textureBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sss);
        textureId = GlUtils.createTextureId(textureBitmap);

    }

    /**
     * 当Surface发生变化的时候回调，比如竖屏转横屏导致GLSurfaceView大小发生变化，
     * 通常情况下在此方法中设置绘制窗口及和GLSurfaceView大小有关系的参数。
     */
    @Override

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;
        initProjectionMatrix();
    }

    /**
     * 执行OpenGL ES渲染工作，由系统以一定的频率来调用重绘View，
     * 当设置GLSurfaceView的渲染模式为GLSurfaceView.RENDERMODE_CONTINUOUSLY或不设置时，系统就会主动回调onDrawFrame()方法,
     * 如果设置为 RENDERMODE_WHEN_DIRTY ，手动调用requestRender()，才会渲染
     */
    @Override
    public void onDrawFrame(GL10 gl) {
//        Log.e("Better", "onDrawFrame" + rotateX + "," + rotateY + "," + rotateZ);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(program);

        prepareDraw();

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

    public void prepareDraw() {
        initProjectionMatrix();
        setIdentityM(modeMatrix, 0);
        rotateM(modeMatrix, 0, rotateX, 1f, 0f, 0f);
        rotateM(modeMatrix, 0, rotateY, 0f, 1f, 0f);
        rotateM(modeMatrix, 0, rotateZ, 0f, 0f, 1f);
        float[] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modeMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
        glUniformMatrix4fv(uMvpMatrixLoc, 1, false, projectionMatrix, 0);
    }

    private void initProjectionMatrix() {
        //构建透视投影矩阵
        CommonUtils.perspectiveM(projectionMatrix, 45, ratio, 1f, 10f);
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
