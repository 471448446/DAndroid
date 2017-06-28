package better.glsurfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * GlSurfaceView 相当于是画布，Render是具体的怎样画，渲染。
 * 渲染模式：
 * RENDERMODE_WHEN_DIRTY  懒惰渲染，需要手动调用 glSurfaceView.requestRender() 才会进行更新
 * RENDERMODE_CONTINUOUSLY 不停渲染
 *
 * Created by better on 2017/4/26 13:30.
 */

public class BGlSurfaceView extends GLSurfaceView {
    public BGlSurfaceView(Context context) {
        super(context);
        init();
    }

    public BGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        setEGLContextClientVersion(2);
        setRenderer(new GlSurfaceRender());

        /**
         * Render the view only when there is a change in the drawing data
         * 改变的时候才画
         */
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }
}
