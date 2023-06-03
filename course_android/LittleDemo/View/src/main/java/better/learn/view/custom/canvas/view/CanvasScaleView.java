package better.learn.view.custom.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.ICustomView;

/**
 * 缩放默认是圆点，缩放就是对应轴乘缩放值
 * Created by better on 2017/9/19 11:24.
 */

public class CanvasScaleView extends View implements ICustomView {
    Paint mPaint = new Paint();
    int circleX = 150;
    int circleY = 150;

    RectF mRectFCenter = new RectF(-100, -100, 100, 100);

    RectF mRectFOneQuadrant = new RectF(100, 100, 200, 200);
    RectF mRectFTwoQuadrant = new RectF(-200, 100, -100, 200);
    RectF mRectFThreeQuadrant = new RectF(-200, -200, -100, -100);
    RectF mRectFFourQuadrant = new RectF(100, -200, 200, -100);

    float centerScale = 0.9f;
    float outScale = 1.1f;

    public CanvasScaleView(Context context) {
        super(context);
        init();
    }

    public CanvasScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        canvas.drawCircle(circleX, circleY, 100, mPaint);

        canvas.scale(0.5f, 0.5f);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        canvas.drawCircle(circleX, circleY, 100, mPaint);
        //还原
        canvas.scale(2, 2);

        canvas.scale(0.5f, 0.5f, circleX, circleY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        canvas.drawCircle(circleX, circleY, 100, mPaint);
        //还原
        canvas.scale(2, 2, circleX, circleY);

        if (0 == getWidth() || 0 == getHeight()) return;
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(1, -1);

        mPaint.setColor(Color.BLACK);
        for (int i = 0; i < 20; i++) {
            canvas.drawRect(mRectFCenter, mPaint);
            canvas.scale(centerScale, centerScale);
        }
        resetScale(canvas, 1 / centerScale, 20);

        for (int i = 0; i < 5; i++) {
            canvas.drawRect(mRectFOneQuadrant, mPaint);
            canvas.scale(outScale, outScale);
        }
        resetScale(canvas, 1 / outScale, 5);

        for (int i = 0; i < 5; i++) {
            canvas.drawRect(mRectFTwoQuadrant, mPaint);
            canvas.scale(outScale, outScale);
        }
        resetScale(canvas, 1 / outScale, 5);

        for (int i = 0; i < 5; i++) {
            canvas.drawRect(mRectFThreeQuadrant, mPaint);
            canvas.scale(outScale, outScale);
        }
        resetScale(canvas, 1 / outScale, 5);

        for (int i = 0; i < 5; i++) {
            canvas.drawRect(mRectFFourQuadrant, mPaint);
            canvas.scale(outScale, outScale);
        }
        resetScale(canvas, 1 / outScale, 5);
    }

    private void resetScale(Canvas canvas, float scale, int count) {
        for (int i = 0; i < count; i++) {
            canvas.scale(scale, scale);
        }
    }
}
