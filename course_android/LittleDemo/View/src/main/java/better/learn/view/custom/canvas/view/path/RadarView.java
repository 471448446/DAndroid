package better.learn.view.custom.canvas.view.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.ICustomView;

/**
 * Created by better on 2017/9/22 13:09.
 */

public class RadarView extends View implements ICustomView {
    Paint mPaint = new Paint();
    Path mPath = new Path();
    Path mPathRadar = new Path();

    float miniSpiderSide = 50;
    int maxSpiderCycleCount = 0;
    float spiderArc = 60;

    float[] radarPoints;

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public void init() {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxSpiderCycleCount = (int) ((Math.min(getWidth(), getHeight()) / 2 - 50) / miniSpiderSide);
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(maxSpiderCycleCount * miniSpiderSide, 0);
        //弧度
        double arc = 2 * Math.PI / 360 * spiderArc;
        for (int i = 1; i <= maxSpiderCycleCount; i++) {
            mPath.moveTo(i * miniSpiderSide, 0);
            mPath.lineTo((float) (i * miniSpiderSide * Math.cos(arc)),
                    (float) (Math.tan(arc) * Math.cos(arc) * i * miniSpiderSide));
        }

        radarPoints = new float[]{
                getX(1, 3, arc), getY(1, 3, arc),
                getX(2, maxSpiderCycleCount, arc), getY(2, maxSpiderCycleCount, arc),
                getX(3, 3, arc), getY(3, 3, arc),
                getX(4, 2, arc), getY(4, 2, arc),
                getX(5, 1, arc), getY(5, 1, arc),
                getX(6, 4, arc), getY(6, 4, arc),
        };
        mPathRadar.reset();
        mPathRadar.moveTo(radarPoints[0], radarPoints[1]);
        for (int i = 1; i <= (radarPoints.length / 2 - 1); i++) {
            mPathRadar.lineTo(radarPoints[i * 2], radarPoints[i * 2 + 1]);
        }
        mPathRadar.close();
    }

    private float getX(int indexArc/*begin=1第一个角度后的线*/, int indexSlide/*begin=1*/, double arc) {
        return (float) (indexSlide * miniSpiderSide * Math.cos(indexArc * arc));
    }

    private float getY(int indexArc, int indexSlide, double arc) {
        return (float) (Math.tan(arc * indexArc) * Math.cos(arc * indexArc) * indexSlide * miniSpiderSide);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (0 == getWidth() || 0 == getHeight()) return;
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(1, -1);

        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        for (int i = 0; i < (360 / spiderArc); i++) {
            canvas.drawPath(mPath, mPaint);
            canvas.rotate(spiderArc);
        }

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        canvas.drawPoints(radarPoints, mPaint);

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorArc));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPathRadar, mPaint);

    }
}
