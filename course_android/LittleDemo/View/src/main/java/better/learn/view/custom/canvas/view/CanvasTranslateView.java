package better.learn.view.custom.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.ICustomView;

/**
 * 位移是基于当前位置移动，而不是每次基于屏幕左上角的(0,0)点移动
 * 下次onDraw并不受影响Canvas
 * Created by better on 2017/9/19 11:05.
 */

public class CanvasTranslateView extends View implements ICustomView {
    Paint mPaint = new Paint();
    int radius = 100;
    int translate = 100;

    public CanvasTranslateView(Context context) {
        super(context);
        init();
    }

    public CanvasTranslateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasTranslateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorArc));
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 3; i++) {
            canvas.translate(translate, translate);
            canvas.drawCircle(0, 0, radius, mPaint);
        }
        //还原
        canvas.translate(-translate * 3, -translate * 3);

        if (0 == getWidth() || 0 == getHeight()) {
            return;
        }
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawCircle(0, 0, radius, mPaint);
        canvas.drawCircle(-radius, 0, radius, mPaint);
        canvas.drawCircle(0, radius, radius, mPaint);
        canvas.drawCircle(radius, 0, radius, mPaint);
        canvas.drawCircle(0, -radius, radius, mPaint);
    }
}
