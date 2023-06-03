package better.learn.view.custom.bezier.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by better on 2017/9/14 15:17.
 */

public class BezierThreeView extends BezierView {
    PointF controlPoint1, controlPoint2;

    public BezierThreeView(Context context) {
        super(context);
    }

    public BezierThreeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BezierThreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void init() {
        super.init();
        controlPoint1 = new PointF();
        controlPoint2 = new PointF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        controlPoint1.x = w / 4;
        controlPoint1.y = h / 4;
        controlPoint2.x = w / 2;
        controlPoint2.y = h / 5;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float one = Math.abs((event.getX() - startPoint.x) / (event.getY() - startPoint.y));
        float two = Math.abs((event.getX() - endPoint.x) / (event.getY() - endPoint.y));
        if (one < two) {
            controlPoint1.x = event.getX();
            controlPoint1.y = event.getY();
        } else {
            controlPoint2.x = event.getX();
            controlPoint2.y = event.getY();
        }
        invalidate();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // 控制点
        paint.setStrokeWidth(10);
        canvas.drawPoint(startPoint.x, startPoint.y, paint);
        canvas.drawPoint(endPoint.x, endPoint.y, paint);
        canvas.drawPoint(controlPoint1.x, controlPoint1.y, paint);
        canvas.drawPoint(controlPoint2.x, controlPoint2.y, paint);
        // 辅助线
        paint.setStrokeWidth(4);
        canvas.drawLine(startPoint.x, startPoint.y, controlPoint1.x, controlPoint1.y, paint);
        canvas.drawLine(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, paint);
        canvas.drawLine(endPoint.x, endPoint.y, controlPoint2.x, controlPoint2.y, paint);

        canvas.drawText("起", startPoint.x - 20, startPoint.y, paint);
        canvas.drawText("终", endPoint.x + 10, endPoint.y, paint);
        canvas.drawText("控制点1", controlPoint1.x, controlPoint1.y - 10, paint);
        canvas.drawText("控制点2", controlPoint2.x, controlPoint2.y - 10, paint);

        //曲线
        path.reset();
        path.moveTo(startPoint.x, startPoint.y);
        path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, endPoint.x, endPoint.y);
        canvas.drawPath(path, paintCurve);
    }
}
