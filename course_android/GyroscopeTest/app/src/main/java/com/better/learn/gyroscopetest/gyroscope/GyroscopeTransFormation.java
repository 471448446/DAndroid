package com.better.learn.gyroscopetest.gyroscope;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

//import com.squareup.picasso.Transformation;

/**
 * 根据控件的宽高 来拉伸 支持陀螺仪滚动图片的Transformation
 */

public class GyroscopeTransFormation extends BitmapTransformation {
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.GyroscopeTransFormation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int mWidgetWidth;   //控件宽度
    private int mWidgetHeight;  //控件高度
    private double mTargetWidth;    //目标宽度
    private double mTargetHeight;   //目标高度

    public GyroscopeTransFormation(int widgetWidth, int widgetHeight) {
        mWidgetWidth = widgetWidth;
        mWidgetHeight = widgetHeight;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap source, int outWidth, int outHeight) {

        if (source.getWidth() == 0 || source.getHeight() == 0) {
            return source;
        }

        mTargetWidth = source.getWidth();
        mTargetHeight = source.getHeight();

        double ratio = mTargetWidth / mTargetHeight;     //图片的宽高比
        int distance;                                    //图片缩放后与控件边的距离

        if (mWidgetHeight <= mWidgetWidth) {
            distance = mWidgetHeight / 8;
            mTargetWidth = mWidgetWidth + 2 * distance;
            mTargetHeight = mTargetWidth / ratio;
        } else {
            distance = mWidgetWidth / 8;
            mTargetHeight = mWidgetHeight + 2 * distance;
            mTargetWidth = mTargetHeight * ratio;
        }

        int desiredWidth = (int) mTargetWidth;
        int desiredHeight = (int) mTargetHeight;

        Bitmap result = Bitmap.createScaledBitmap(source, desiredWidth, desiredHeight, false);

        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GyroscopeTransFormation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

}
