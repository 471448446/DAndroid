package com.better.learn.gl20.training.texture.copy;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.better.learn.gl20.App;
import com.better.learn.gl20.R;
import com.better.learn.gl20.fuck.CommonUtils;
import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;

/**
 * 正方体的绘制，增加透视投影和纹理贴图
 */
public class CubeRotateRender implements GLSurfaceView.Renderer {
    //顶点shader
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +            //顶点坐标
                    "uniform mat4 vMatrix;" +        //变换矩阵
                    "attribute vec2 aTextureCoords;" +   //纹理坐标，传入
                    "varying vec2 vTextureCoords;" +     //纹理坐标，传递到片断shader
                    "void main(){" +                         //每个shader中必须有一个main函数
                    "gl_Position = vMatrix*vPosition;" +     //坐标变换赋值
                    "vTextureCoords = aTextureCoords;" +     //传递纹理坐标
                    "}";

    //片断shader
    private final String fragmentShaderCode =
            "precision mediump float;" +                     //精度设置为mediump
                    "uniform sampler2D uTextureUnit;" +      //纹理单元
                    "varying vec2 vTextureCoords;" +         //纹理坐标，由顶点shader传递过来
                    "void main(){" +                             //每个shader中必须有一个main函数
                    "gl_FragColor = texture2D(uTextureUnit, vTextureCoords);" + //赋值2D纹理
                    "}";

    private int mMatrixHandle;
    private int mPositionHandle;
    private int mTextureHandle;
    private int mTextureCoordHandle;

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureCoordBuffer;
    private int program;
    private int texture;


    private int vertexShader, fragmentShader;

    private float s = 0.5f;
    //正方体顶点坐标
    private float[] coords = {
            -s, s, -s, s, s, s, s, s, -s,       //023
            -s, s, -s, -s, s, s, s, s, s,       //012
            -s, s, s, -s, -s, s, s, s, s,       //152
            s, s, s, -s, -s, s, s, -s, s,       //256
            s, s, s, s, -s, s, s, -s, -s,       //267
            s, s, s, s, -s, -s, s, s, -s,       //273
            s, s, -s, s, -s, -s, -s, -s, -s,    //374
            s, s, -s, -s, -s, -s, -s, s, -s,    //340
            -s, s, -s, -s, -s, -s, -s, s, s,    //041
            -s, s, s, -s, -s, -s, -s, -s, s,    //145
            -s, -s, s, -s, -s, -s, s, -s, s,    //546
            s, -s, s, -s, -s, -s, s, -s, -s     //647

//            -s,s,-s,    // 0
//            -s,s,s,     // 1
//            s,s,s,      // 2
//            s,s,-s,     // 3
//            -s,-s,-s,   // 4
//            -s,-s,s,    // 5
//            s,-s,s,     // 6
//            s,-s,-s,    // 7
    };

    //纹理坐标
    private float[] textureCoord = {

            0, 1, 1, 0, 1, 1,
            0, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 1,
            1, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 0,
            0, 1, 1, 0, 1, 1,
            0, 1, 0, 0, 1, 0,
            0, 1, 1, 0, 1, 1,
            0, 1, 0, 0, 1, 1,
            1, 1, 0, 0, 1, 0,
            0, 1, 0, 0, 1, 1,
            1, 1, 0, 0, 1, 0
    };
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

    public CubeRotateRender() {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);
        //打开深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //逆时针为正面
        GLES20.glFrontFace(GLES20.GL_CCW);
        //打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //背面剪裁
        GLES20.glCullFace(GLES20.GL_BACK);

        // 初始化缓冲数据
        vertexBuffer = GlUtils.arrayToFloatBuffer(coords);
        textureCoordBuffer = GlUtils.arrayToFloatBuffer(textureCoord);

        // 编译shader代码
        vertexShader = GlUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        fragmentShader = GlUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        //加载纹理贴图
        texture = GlUtils.createTextureId(App.application, R.mipmap.ss);

        // 创建Program
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置视窗大小及位置
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;
        initProjectionMatrix();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除深度缓冲与颜色缓冲
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // 添加program到OpenGL ES环境中
        GLES20.glUseProgram(program);

        mMatrixHandle = GLES20.glGetUniformLocation(program, "vMatrix");
        mTextureHandle = GLES20.glGetUniformLocation(program, "uTextureUnit");
        mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        mTextureCoordHandle = GLES20.glGetAttribLocation(program, "aTextureCoords");

        prepareDraw();
        //矩阵变换
//        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mVMatrix, 0);

        //设置纹理
        //激活纹理单元，GL_TEXTURE0代表纹理单元0，GL_TEXTURE1代表纹理单元1，以此类推。OpenGL使用纹理单元来表示被绘制的纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //绑定纹理到这个纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        //把选定的纹理单元传给片段着色器中的u_TextureUnit，
        GLES20.glUniform1i(mTextureHandle, 0);

        //顶点坐标
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        //纹理坐标
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureCoordBuffer);

        //绘制图形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, coords.length / 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
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
        glUniformMatrix4fv(mMatrixHandle, 1, false, projectionMatrix, 0);
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
