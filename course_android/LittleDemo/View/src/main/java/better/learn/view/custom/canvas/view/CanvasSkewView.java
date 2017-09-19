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
 * Created by better on 2017/9/19 15:46.
 */

public class CanvasSkewView extends View implements ICustomView {
    Paint mPaint = new Paint();

    RectF mRectF = new RectF(0, 0, 100, 100);

    public CanvasSkewView(Context context) {
        super(context);
        init();
    }

    public CanvasSkewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CanvasSkewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.scale(1, -1);
        for (int i = 0; i < 3; i++) {
            canvas.drawRect(mRectF, mPaint);
            canvas.skew(1, 0);
        }
    }
}
