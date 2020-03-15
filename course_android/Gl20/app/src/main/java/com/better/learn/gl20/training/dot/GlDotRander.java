package com.better.learn.gl20.training.dot;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.better.learn.gl20.R;
import com.better.learn.gl20.training.GlUtils;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * openGl坐标：
 * 顶点坐标轴以屏幕中心为原点（0，0），z轴的正方向为穿透屏幕指向外面。x 朝有方向，y朝上方向
 * 取值范围是[-1,1]
 * https://www.jianshu.com/p/890aa27b8dbc
 * https://www.jianshu.com/p/51ef66d53111
 * Created by better on 2020/3/15 9:46 PM.
 */
public class GlDotRander implements GLSurfaceView.Renderer {
    private Context context;

    // 顶点数据 圆点
    float coords[] = {
            -0f, 0f, 0f
    };
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    private FloatBuffer vertBuffer;
    private FloatBuffer colorBuffer;

    // gl相关
    private int program;
    private int vPositionLoc;
    private int uColorLoc;

    public GlDotRander(Context context) {
        this.context = context;
        vertBuffer = GlUtils.arrayToFloatBuffer(coords);
        //  r,g,b,a
        colorBuffer = GlUtils.arrayToFloatBuffer(color);
    }

    /**
     * GLSurfaceView创建完成，也代表OpenGL ES环境创建完成，通常情况下在此方法中创建Program及初始化参数。
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        program = GlUtils.createProgram(context, R.raw.point_vertex_shader, R.raw.point_fragment_shader);
        vPositionLoc = GLES20.glGetAttribLocation(program, "vPosition");
        uColorLoc = GLES20.glGetAttribLocation(program, "vColor");
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
        GLES20.glEnableVertexAttribArray(vPositionLoc);
        GLES20.glVertexAttribPointer(vPositionLoc, 3, GLES20.GL_FLOAT, false, 0, vertBuffer);

        GLES20.glUniform4fv(uColorLoc, 1, colorBuffer);
        /**
         * 第一个参数mode，表示绘制的方式，可选择的值有：GL_POINTS、GL_LINES、GL_LINE_LOOP、GL_LINE_STRIP、GL_TRIANGLES、GL_TRIANGLE_STRIP、GL_TRIANGLE_FAN。
         * 第二个参数表示从数组缓存中的哪一位开始绘制，一般为0。
         * 第三个参数表示绘制顶点的数量。
         */
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }
}
