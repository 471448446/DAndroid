package better.learn.view.custom.bezier.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by better on 2017/9/14 15:53.
 */

public class HeartMoveView extends BezierView {
    final int MSG = 10;
    int radius;
    PointF topPoint = new PointF(), bottomPoint = new PointF(),
            controlPointLeft = new PointF(), controlPointRight = new PointF();
    Path leftPath = new Path(), rightPath = new Path();

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (getHeight() / 2 - topPoint.y > (float) radius / 4 * 3) {
                topPoint.y += 10;
                invalidate();
                mHandler.sendEmptyMessageDelayed(MSG, 400);
            }
            return false;
        }
    });

    public HeartMoveView(Context context) {
        super(context);
    }

    public HeartMoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartMoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 4;
        topPoint.x = w / 2;
        topPoint.y = h / 2 - radius;
        bottomPoint.x = w / 2;
        bottomPoint.y = h / 2 + radius;
        controlPointLeft.x = w / 2 - radius;
        controlPointLeft.y = h / 2;
        controlPointRight.x = w / 2 + radius;
        controlPointRight.y = h / 2;
        mHandler.sendEmptyMessageDelayed(MSG, 500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (topPoint.y == getHeight() / 2 - radius) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paintCurve);
            return;
        }
        leftPath.reset();
        leftPath.moveTo(bottomPoint.x, bottomPoint.y);
        leftPath.quadTo(controlPointLeft.x, controlPointLeft.y, topPoint.x, topPoint.y);
        rightPath.reset();
        rightPath.moveTo(bottomPoint.x, bottomPoint.y);
        rightPath.quadTo(controlPointRight.x, controlPointRight.y, topPoint.x, topPoint.y);
        canvas.drawPath(leftPath, paintCurve);
        canvas.drawPath(rightPath, paintCurve);
    }
}
