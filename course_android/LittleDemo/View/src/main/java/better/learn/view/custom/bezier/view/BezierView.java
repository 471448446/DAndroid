package better.learn.view.custom.bezier.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;

/**
 * Created by better on 2017/9/14 14:16.
 */

public abstract class BezierView extends View {

    Paint paint, paintCurve;
    Path path;

    PointF startPoint, endPoint;

    public BezierView(Context context) {
        super(context);
        init();
    }

    void init() {
        paint = new Paint();
        paintCurve = new Paint();
        path = new Path();
        startPoint = new PointF();
        endPoint = new PointF();

        paint.setColor(Color.GRAY);
        paintCurve.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setTextSize(18);

        //设置空心，不然会画满
        paintCurve.setStyle(Paint.Style.STROKE);
        paintCurve.setAntiAlias(true);
        paintCurve.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        paintCurve.setStrokeWidth(6);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startPoint.x = w / 2 - 200;
        startPoint.y = h / 2;
        endPoint.x = w / 2 + 200;
        endPoint.y = h / 2;

    }
}
