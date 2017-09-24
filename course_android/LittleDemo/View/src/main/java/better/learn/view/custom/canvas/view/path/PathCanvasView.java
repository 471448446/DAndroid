package better.learn.view.custom.canvas.view.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.ICustomView;

/**
 * Path: 一组直线或曲线的路径集合
 * 添加图形有顺时针和逆时针区别==》因为Path是按照一条线或一条曲线来画的==》可用setLastPoint来看出区别
 * 1moveTo、 setLastPoint、 lineTo 和 close
 * 2addXxx与arcTo
 * 2.1addPath
 * 2.2addArc与arcTo
 * 3isEmpty、 isRect、isConvex、 set 和 offset
 * Created by better on 2017/9/22 10:03.
 */

public class PathCanvasView extends View implements ICustomView {
    Paint mPaint = new Paint();
    Path mPath = new Path();

    public PathCanvasView(Context context) {
        super(context);
        init();
    }

    public PathCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public PathCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public void init() {
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1111
        mPath.reset();
        mPath.moveTo(50, 50);
        mPath.lineTo(200, 200);
        mPath.lineTo(200, 50);
        mPath.close();
        // 这里会改变最后一次的着陆点200，50改为100，50。
//        mPath.setLastPoint(100, 50);
        canvas.drawPath(mPath, mPaint);

        //2222
        mPath.reset();
        mPath.addCircle(300, 100, 100, Path.Direction.CW/*顺时针*/);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();

        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(1, -1);
        mPaint.setColor(Color.GRAY);
        canvas.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0, mPaint);
        canvas.drawLine(0, -getHeight() / 2, 0, getHeight() / 2, mPaint);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        mPath.reset();
        mPath.addRect(-200, -200, 200, 200, Path.Direction.CCW);
        mPath.setLastPoint(-100, 100);
        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        mPath.lineTo(-50, 50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPath.arcTo(-100, 0, 0, 100, 30, 60, false/*连接最后一个点与圆弧起点*/);
//            mPath.addArc(-100, 0, 0, 100, 30, 60);
            canvas.drawPath(mPath, mPaint);
        }
        mPath.reset();
        mPath.lineTo(50, 50);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPath.arcTo(0, 0, 100, 100, 30, 60, true/*不连接最后一个点与圆弧起点*/);
//            mPath.addArc(0, 0, 100, 100, 30, 60);
            canvas.drawPath(mPath, mPaint);
        }


    }
}
