package com.example.better.linechartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

//裁剪了区域以后，绘制的left和top仍然是以canvas的大小为准，这样就能实现只绘制图片的某一部分的需求，有点类似ps里面的遮罩效果。
public class BarChatView extends View {
    private static final float BottomSpace = 20;
    private static final float LeftSpace = 0;
    private static final int xyTxtSize = 10;
    private float yMaxCapacity = 3000;

    private RectF barAreaRect = new RectF(),
            xAreaRect = new RectF();
    private Paint paint = new Paint();
    private Path path = new Path();

    private List<BarPointInfo> points = new ArrayList<>();
    private float barW = 40;
    private float barDistance;
    private float barOffset = 20;
    private int barColor = 0xFFDEEDFA;
    private int xyColor = Color.BLACK;
    private int xyTxtColor = Color.BLACK;
    private OnClickBarListener onClickBarListener;

    public BarChatView setOnClickBarListener(OnClickBarListener onClickBarListener) {
        this.onClickBarListener = onClickBarListener;
        return this;
    }

    public BarChatView setPoints(List<BarPointInfo> points) {
        this.points = points;
        calculate();
        invalidate();
        return this;
    }

    public BarChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        points.add(new BarPointInfo("8/8", 2999));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setTextSize(ViewUtil.size2sp(xyTxtSize, getContext()));
        Rect bound = new Rect();
        String date = "12/12";
        paint.getTextBounds(date, 0, date.length(), bound);
        if (barW < bound.width()) {
            barW = bound.width();
        }

        xAreaRect.left = getPaddingLeft() + LeftSpace/*+Y轴文字宽*/;
        xAreaRect.top = getHeight() - getPaddingBottom() - bound.height() - BottomSpace;
        xAreaRect.right = getWidth() - getPaddingRight()/*-Y轴文字宽*/;
        xAreaRect.bottom = getHeight() - getPaddingBottom();

        barAreaRect.left = xAreaRect.left;
        barAreaRect.top = getPaddingTop();
        barAreaRect.right = xAreaRect.right;
        barAreaRect.bottom = xAreaRect.top;

        barDistance = (barAreaRect.width() - barOffset * 2 - barW * 7) / 6;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawRect(barAreaRect, paint);
//        canvas.drawRect(xAreaRect, paint);
        drawX(canvas);
        drawY(canvas);
        drawBar(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                calculateClick(event.getX(), event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }

    private void calculateClick(float x, float y) {
        if (!barAreaRect.contains(x, y)) {
            return;
        }
        for (int i = 0, l = points.size(); i < l; i++) {
            float s = barAreaRect.left + barOffset + (barDistance + barW) * i;
            if (x >= s && x <= s + barW && y >= barAreaRect.top + (barAreaRect.height() - getTransformY(points.get(i).y))) {
                if (null != onClickBarListener) {
                    onClickBarListener.onClickBar(i, points.get(i));
                }
                break;
            }
        }
    }

    private void drawBar(Canvas canvas) {
        path.reset();
        paint.setColor(barColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.save();
        canvas.translate(barAreaRect.left, barAreaRect.bottom);
        canvas.scale(1, -1);
        float x, y;
        for (int i = 0, l = points.size(); i < l; i++) {
            x = barOffset + (barDistance + barW) * i;
            y = getTransformY(points.get(i).y);
            path.moveTo(x, 2);
            path.lineTo(x, y);
            path.lineTo(x + barW, y);
            path.lineTo(x + barW, 2);
            path.close();
        }
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private void calculate() {
        for (BarPointInfo f : points) {
            if (f.y > yMaxCapacity) {
                yMaxCapacity = f.y;
            }
        }
    }

    private void drawY(Canvas canvas) {

    }

    private void drawX(Canvas canvas) {
        paint.setColor(xyColor);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(xAreaRect.left, xAreaRect.top, xAreaRect.right, xAreaRect.top, paint);

        paint.setColor(xyTxtColor);
        float x;
        for (int i = 0, l = points.size(); i < l; i++) {
            x = barAreaRect.left + barOffset + (barDistance + barW) * i;
            x += barW / 2 - paint.measureText(points.get(i).x) / 2;
            canvas.drawText(points.get(i).x, x, xAreaRect.bottom, paint);
        }
    }

    private float getTransformY(float y) {
        return y / yMaxCapacity * barAreaRect.height();
    }

    public interface OnClickBarListener {
        void onClickBar(int index, BarPointInfo pointInfo);
    }

    public static class BarPointInfo {
        public String x;
        public float y;

        public BarPointInfo(String x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
