package better.learn.view.custom.bezier.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * moveTo
 * quadTo
 * Created by better on 2017/9/14 14:14.
 */

public class BezierTwoView extends BezierView {
    PointF controlPoint;

    public BezierTwoView(Context context) {
        super(context);
    }

    public BezierTwoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BezierTwoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void init() {
        super.init();
        controlPoint = new PointF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        controlPoint.x = w / 4;
        controlPoint.y = h / 4;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controlPoint.x = event.getX();
        controlPoint.y = event.getY();
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 控制点
        paint.setStrokeWidth(10);
        canvas.drawPoint(startPoint.x, startPoint.y, paint);
        canvas.drawPoint(endPoint.x, endPoint.y, paint);
        canvas.drawPoint(controlPoint.x, controlPoint.y, paint);
        // 辅助线
        paint.setStrokeWidth(4);
        canvas.drawLine(startPoint.x, startPoint.y, controlPoint.x, controlPoint.y, paint);
        canvas.drawLine(endPoint.x, endPoint.y, controlPoint.x, controlPoint.y, paint);

        canvas.drawText("起", startPoint.x - 20, startPoint.y, paint);
        canvas.drawText("终", endPoint.x + 10, endPoint.y, paint);
        canvas.drawText("控制点", controlPoint.x, controlPoint.y - 10, paint);

        //曲线
        path.reset();
        path.moveTo(startPoint.x, startPoint.y);
        path.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(path, paintCurve);
    }
}
