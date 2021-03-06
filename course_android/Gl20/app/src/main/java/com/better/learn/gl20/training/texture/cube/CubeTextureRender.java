package com.better.learn.gl20.training.texture.cube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.better.learn.gl20.R;
import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * https://learnopengl-cn.github.io/intro/
 * Android OpenGL ES 视频应用开发教程目录: https://www.jianshu.com/p/d134a835ebec
 * 顶点索引：
 * Android OpenGL ES顶点坐标、纹理贴图坐标设置: https://blog.csdn.net/zhangjikuan/article/details/23126639
 * Android OpenGL ES 6.索引法绘制: https://www.jianshu.com/p/92c12166a935
 * <p>
 * https://www.jianshu.com/u/eb01968a6673
 * es 1.0 :http://www.guidebee.info/wordpress/?page_id=2376
 * 立方体+纹理实现 es2.0: https://linuxparachen.gitbooks.io/android-opengl-es-2-0/content/%E7%AC%AC%E4%BA%8C%E9%83%A8%E5%88%86%EF%BC%9AOpenGL%20ES%20%E5%BC%80%E5%8F%91%E5%AE%9E%E4%BE%8B/2.3%20%E7%AB%8B%E6%96%B9%E4%BD%93+%E7%BA%B9%E7%90%86%E5%AE%9E%E7%8E%B0.html
 * Created by better on 2020/3/15 10:22 PM.
 */
public class CubeTextureRender implements GLSurfaceView.Renderer {
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

    // 顶点数据 圆点
    private float r = 0.5f;
    private float coordsVertex[] = {
            r, r, r,     //正面右上0
            r, -r, r,    //正面右下1
            -r, -r, r,   //正面左下2
            -r, r, r,    //正面左上3
            r, r, -r,     //反面右上4
            r, -r, -r,    //反面右下5
            -r, -r, -r,   //反面左下6
            -r, r, -r,    //反面左上7
    };
    private short[] indexVertex = {
            2, 1, 0, 2, 0, 3,    //正面2103
            6, 7, 4, 6, 4, 5,    //后面6745
            3, 0, 4, 3, 4, 7,    //上面3047
            2, 6, 5, 2, 5, 1,    //下面2651
            6, 2, 3, 6, 3, 7,    //左面6237
            5, 4, 0, 5, 0, 1,    //右面5401
    };

    //纹理坐标的原点是左上角，右下角是（1，1）
    private float[] coordsTexture = {
            0, 1, 1, 1, 1, 0, 0, 0, //前面
            0, 1, 0, 0, 1, 0, 1, 1, //后面 
            0, 1, 1, 1, 1, 0, 0, 0, //上面   
            0, 0, 0, 1, 1, 1, 1, 0, //下面  
            0, 1, 1, 1, 1, 0, 0, 0, //左面
            1, 1, 1, 0, 0, 0, 0, 1, //右面
    };

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
    // 摄像头（观察）矩阵
    private final float[] viewMatrix = new float[16];
    // 变换矩阵
    private final float[] vPMatrix = new float[16];
    private float[] rotationMatrix = new float[16];

    private float roateX, roateY, roateZ;

    public void setRoateX(float roateX) {
        this.roateX = roateX;
    }

    public void setRoateY(float roateY) {
        this.roateY = roateY;
    }

    public void setRoateZ(float roateZ) {
        this.roateZ = roateZ;
    }

    public CubeTextureRender(Context context) {
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

        float ratio = (float) width / height;

        // 根据平截头体6个面，生成透视投影矩阵
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
//        Matrix.perspectiveM(projectionMatrix,0,40,ratio,3,7);

        // 这俩步目前在这里设置也是没问题的，因为 不会涉及到改变
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 2, 2, -4.2f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    /**
     * 执行OpenGL ES渲染工作，由系统以一定的频率来调用重绘View，
     * 当设置GLSurfaceView的渲染模式为GLSurfaceView.RENDERMODE_CONTINUOUSLY或不设置时，系统就会主动回调onDrawFrame()方法,
     * 如果设置为 RENDERMODE_WHEN_DIRTY ，手动调用requestRender()，才会渲染
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        applyMatrix();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(program);


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

    private void applyMatrix() {
        // 处理矩阵
        float[] scratch = new float[16];
        // Create a rotation transformation for the triangle
        float angle = 05f;
        Matrix.setRotateM(rotationMatrix, 0, roateX, 1f, 0f, 0f);
        Matrix.setRotateM(rotationMatrix, 0, roateY, 0, 1.0f, 0f);
        Matrix.setRotateM(rotationMatrix, 0, roateZ, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        //mvpMatrix = vPMatrix × rotationMatrix
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);
        GLES20.glUniformMatrix4fv(uMvpMatrixLoc, 1, false, scratch, 0);

        // 不旋转
//        GLES20.glUniformMatrix4fv(uMvpMatrixLoc, 1, false, vPMatrix, 0);
    }
}
