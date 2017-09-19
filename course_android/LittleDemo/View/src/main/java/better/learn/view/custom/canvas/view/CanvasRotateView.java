package better.learn.view.custom.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.custom.ICustomView;

/**
 * 还原的时候要注意按步骤还原
 * Created by better on 2017/9/19 15:05.
 */

public class CanvasRotateView extends View implements ICustomView {
    Paint mPaint = new Paint();

    RectF mRectF = new RectF(0, 0, 100, 100);
    RectF mRectFOther = new RectF(200, 200, 300, 300);

    float rotateDefaultArc = 15;
    float rotateCenterArc = 15;

    int radiusInSide = 130;
    int radiusOutSide = 150;
    float rotateDoubleCircle = 10;

    public CanvasRotateView(Context context) {
        super(context);
        init();
    }

    public CanvasRotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CanvasRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public void init() {
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(50, 50);
        for (int i = 0; i < 3; i++) {
            canvas.drawRect(mRectF, mPaint);
            canvas.rotate(rotateDefaultArc);
        }
        //还原
//        canvas.rotate(-rotateDefaultArc * 3);
//        canvas.translate(-50, -50);
        //取出save时的画布状态，这样避免了还原
        canvas.restore();

        for (int i = 0; i < 3; i++) {
            canvas.drawRect(mRectFOther, mPaint);
            canvas.rotate(rotateCenterArc, mRectFOther.width() / 2, mRectFOther.height() / 2);
        }
        //还原
        for (int i = 0; i < 3; i++) {
            canvas.rotate(-rotateCenterArc, mRectFOther.width() / 2, mRectFOther.height() / 2);
        }

        canvas.translate(400, 200);
        for (int i = 0; i < 3; i++) {
            canvas.drawRect(mRectF, mPaint);
            canvas.rotate(rotateCenterArc, mRectF.width() / 2, mRectF.height() / 2);
        }
        canvas.rotate(-rotateCenterArc * 3, mRectF.width() / 2, mRectF.height() / 2);
        canvas.translate(-400, -200);

        if (0 == getWidth() || 0 == getHeight()) return;
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawCircle(0, 0, radiusInSide, mPaint);
        canvas.drawCircle(0, 0, radiusOutSide, mPaint);
        for (int i = 0; i < (int) (360 / rotateDoubleCircle); i++) {
            canvas.drawLine(radiusInSide, 0, radiusOutSide, 0, mPaint);
            canvas.rotate(rotateDoubleCircle);
        }
        //已经回正了
        canvas.drawLine(0, 0, radiusInSide - 20, 0, mPaint);
    }
}
