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
 * 绘制形状
 * drawPoint,drawPoints
 * drawLine,drawLines
 * drawRect
 * drawCircle
 * drawOval
 * drawRoundRect
 * drawArc
 * 使用默认坐标系，以centerX，centerY为假象的圆点坐标
 * Created by better on 2017/9/18 17:09.
 */

public class GraphicsCanvasView extends View implements ICustomView {
    Paint mPaint;
    int colorPoint = Color.parseColor("#24292e");
    int colorLine = Color.GRAY;
    int colorRect = 0x4dffff00;
    int colorCircle = Color.argb(127, 255, 0, 0);
    int colorOval = Color.argb(255, 165, 0, 1);
    int colorRoundRect = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
    int colorArc = ContextCompat.getColor(getContext(), R.color.colorArc);

    int centerX = 300, centerY = 300;

    int pointHalfDistance = 60;
    float[] arrayPoints = new float[]{
            50, 50,
            centerX + pointHalfDistance, centerY + pointHalfDistance,
            centerX - pointHalfDistance, centerY + pointHalfDistance,
            centerX - pointHalfDistance, centerY - pointHalfDistance,
            centerX + pointHalfDistance, centerY - pointHalfDistance
    };

    int lineToPointOffSet = 20;
    float[] lines = new float[]{
            centerX + pointHalfDistance + lineToPointOffSet, centerY + pointHalfDistance + lineToPointOffSet,
            centerX - pointHalfDistance - lineToPointOffSet, centerY + pointHalfDistance + lineToPointOffSet,
            centerX - pointHalfDistance - lineToPointOffSet, centerY + pointHalfDistance + lineToPointOffSet,
            centerX - pointHalfDistance - lineToPointOffSet, centerY - pointHalfDistance - lineToPointOffSet,
            centerX - pointHalfDistance - lineToPointOffSet, centerY - pointHalfDistance - lineToPointOffSet,
            centerX + pointHalfDistance + lineToPointOffSet, centerY - pointHalfDistance - lineToPointOffSet,
            centerX + pointHalfDistance + lineToPointOffSet, centerY - pointHalfDistance - lineToPointOffSet,
            centerX + pointHalfDistance + lineToPointOffSet, centerY + pointHalfDistance + lineToPointOffSet};
    // 确定矩形需要四个点，android中用对角线的坐标来确定矩形，需要注意的是确保with()和height()值是正的不然不会绘制
    int rectToPointOffSet = lineToPointOffSet + 40;
    RectF mRect = new RectF(
            centerX - pointHalfDistance - rectToPointOffSet, centerX - pointHalfDistance - rectToPointOffSet,
            centerX + pointHalfDistance + rectToPointOffSet, centerX + pointHalfDistance + rectToPointOffSet
    );
    float mRadius = (float) (Math.sqrt((Math.pow(mRect.width(), 2) + Math.pow(mRect.height(), 2))) / 2);

    int ovalToPointOffSetX = rectToPointOffSet + 30, rectOvalToPointOffSetY = rectToPointOffSet - 10;
    RectF mRectOval = new RectF(
            centerX - pointHalfDistance - ovalToPointOffSetX, centerX - pointHalfDistance - rectOvalToPointOffSetY,
            centerX + pointHalfDistance + ovalToPointOffSetX, centerX + pointHalfDistance + rectOvalToPointOffSetY
    );
    int roundRectToPointOffSet = ovalToPointOffSetX + 50;
    RectF mRectRoundToPointOffSet = new RectF(
            centerX - pointHalfDistance - roundRectToPointOffSet, centerX - pointHalfDistance - roundRectToPointOffSet,
            centerX + pointHalfDistance + roundRectToPointOffSet, centerX + pointHalfDistance + roundRectToPointOffSet
    );
    // 扇形的直径点
    int arcToPointOffSet = roundRectToPointOffSet + 20;
    RectF mRectFArc = new RectF(
            centerX - pointHalfDistance - arcToPointOffSet, centerX - pointHalfDistance - arcToPointOffSet,
            centerX + pointHalfDistance + arcToPointOffSet, centerX + pointHalfDistance + arcToPointOffSet
    );

    public GraphicsCanvasView(Context context) {
        super(context);
        init();
    }

    public GraphicsCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphicsCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //点
        mPaint.setColor(colorPoint);
        mPaint.setStrokeWidth(10);
        canvas.drawPoint(centerX, centerY, mPaint);
        canvas.drawPoints(arrayPoints, 2, arrayPoints.length - 2, mPaint);
        //线
        mPaint.setColor(colorLine);
        mPaint.setStrokeWidth(4);
        canvas.drawLine(lines[0], lines[1], lines[2], lines[3], mPaint);
        canvas.drawLines(lines, 4, lines.length - 4, mPaint);
        //矩形
        mPaint.setColor(colorRect);
        canvas.drawRect(mRect, mPaint);
        //圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(colorCircle);
        canvas.drawCircle(centerX, centerY, mRadius, mPaint);
        //椭圆
        mPaint.setColor(colorOval);
        canvas.drawOval(mRectOval, mPaint);
        //圆角矩形
        mPaint.setColor(colorRoundRect);
        canvas.drawRoundRect(mRectRoundToPointOffSet, centerX, centerY, mPaint);

        mPaint.setColor(colorArc);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mRectFArc, 0, 45f, true, mPaint);
    }


}
