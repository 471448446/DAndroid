package com.better.learn.rippleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by better on 2020-02-16 16:04.
 */
public class RippleView extends View {
    private Paint paint;
    // view 宽高
    private float width;
    private float height;
    // 声波的圆圈集合
    private List<Circle> ripples;

    // 圆圈扩散的速度
    private int circleSpeed;

    // 默认到圆心的半径
    private int circleInitWith;

    // 圆圈之间的间距
    private int circleDensity;

    // 圆圈的颜色
    private int circleColor, circleColor2;
    // 圆圈颜色的渐变值【0，255】
    private int circleColorAlpha = 255, circleColorAlpha2 = 255;

    // 圆圈是否为渐变模式
    private boolean mIsAlpha;
    private long circleCount = 0;


    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray tya = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
            circleColor = tya.getColor(R.styleable.RippleView_rippleColor, Color.BLUE);
            circleColor2 = tya.getColor(R.styleable.RippleView_rippleColor2, circleColor);
            circleColorAlpha = tya.getInt(R.styleable.RippleView_rippleColorAlpha, circleColorAlpha);
            circleColorAlpha2 = tya.getInt(R.styleable.RippleView_rippleColorAlpha2, circleColorAlpha);
            circleSpeed = tya.getInt(R.styleable.RippleView_rippleSpeed, 1);
            circleDensity = tya.getDimensionPixelSize(R.styleable.RippleView_rippleDensity,
                    UISizeUtil.dpToPx(context, 20));
            mIsAlpha = tya.getBoolean(R.styleable.RippleView_rippleIsAlpha, false);
            circleInitWith = tya.getDimensionPixelSize(R.styleable.RippleView_rippleInitWith, 0);
            tya.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(circleColor);
        paint.setStrokeWidth(circleDensity);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        ripples = new ArrayList<>();
        Circle c = new Circle(circleInitWith);
        ripples.add(c);

        setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (myWidthSpecMode == MeasureSpec.EXACTLY) {
            width = myWidthSpecSize;
        } else {
            width = UISizeUtil.dpToPx(getContext(), 120);
        }

        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            height = myHeightSpecSize;
        } else {
            height = UISizeUtil.dpToPx(getContext(), 120);
        }

        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        drawInCircle(canvas);
        canvas.restore();
        invalidate();

    }

    private void drawInCircle(Canvas canvas) {
        for (int i = ripples.size() - 1; i >= 0; i--) {
            Circle c = ripples.get(i);
            paint.setAlpha(c.alpha);
            canvas.drawCircle(width / 2, height / 2, c.width, paint);

            // 当圆超出View的宽度后删除
            if (c.width >= width / 2) {
                ripples.remove(i);
            } else {
                if (mIsAlpha) {
                    float alpha;
                    if (circleInitWith > 0) {
                        float progress = (c.width - circleInitWith) / (width * 0.5f - circleInitWith);
                        if (progress > 1) {
                            progress = 1;
                        } else if (progress < 0) {
                            progress = 0;
                        }
                        alpha = alpha(c.index) * (1f - progress);
                    } else {
                        float progress = c.width / (width * 0.5f);
                        if (progress > 1) {
                            progress = 1;
                        } else if (progress < 0) {
                            progress = 0;
                        }
                        alpha = alpha(c.index) * (1f - progress);
                    }
                    c.alpha = (int) alpha;
                }
                c.width += circleSpeed;
            }
        }

        if (ripples.size() > 0) {
            // 两个圆之间的间距
            if (ripples.get(ripples.size() - 1).width - circleInitWith >= circleDensity) {
                ripples.add(new Circle(circleInitWith));
            }
        }
    }

    private int color(long index) {
        if (index % 2 == 0) {
            return circleColor;
        } else {
            return circleColor2;
        }
    }

    private int alpha(long index) {
        if (index % 2 == 0) {
            return circleColorAlpha;
        } else {
            return circleColorAlpha2;
        }
    }

    class Circle {

        long index;

        int width;

        int color;

        int alpha;

        public Circle(int width) {
            this.index = circleCount;
            this.width = width;
            color = color(index);
            alpha = alpha(index);
            circleCount++;
        }
    }

}
