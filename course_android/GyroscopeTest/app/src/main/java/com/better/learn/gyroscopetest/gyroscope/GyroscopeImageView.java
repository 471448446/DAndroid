package com.better.learn.gyroscopetest.gyroscope;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class GyroscopeImageView extends AppCompatImageView {

    private double mScaleX;
    private double mScaleY;
    private float mLenX;
    private float mLenY;
    protected double mAngleX;
    protected double mAngleY;
    private float mOffsetX;
    private float mOffsetY;

    public GyroscopeImageView(Context context) {
        super(context);
        init();
    }

    public GyroscopeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GyroscopeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.CENTER);
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        GyroscopeManager gyroscopeManager = GyroscopeManager.getInstance();
        if (gyroscopeManager != null) {
            gyroscopeManager.addView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        GyroscopeManager gyroscopeManager = GyroscopeManager.getInstance();
        if (gyroscopeManager != null) {
            gyroscopeManager.removeView(this);
        }
    }

    public void update(double scaleX, double scaleY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
        Log.e("Better", "onSensorChanged x? y?" + scaleX + "," +scaleY);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        if (getDrawable() != null) {
            int drawableWidth = getDrawable().getIntrinsicWidth();
            int drawableHeight = getDrawable().getIntrinsicHeight();
            mLenX = Math.abs((drawableWidth - width) * 0.5f);
            mLenY = Math.abs((drawableHeight - height) * 0.5f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null || isInEditMode()) {
            super.onDraw(canvas);
            return;
        }
        mOffsetX = (float) (mLenX * mScaleX);
        mOffsetY = (float) (mLenY * mScaleY);
//        Log.e("Better", "onSensorChanged x? y?" + mOffsetX + "," +mOffsetY);
//        Log.e("Better", "onSensorChanged x? y?" + mScaleX + "," +mScaleY);

        canvas.save();
        canvas.translate(mOffsetX, mOffsetY);
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
