package better.learn.view.custom.matrix.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.ICustomView;

/**
 * 基本的四个操作，以及对应的set、post、pre
 * 除了translate之外，rotate，scale，skew都是相对坐标原点来操作的
 * :
 * 将图片居中
 * 演示四个操作
 * Created by better on 2017/9/28 11:16.
 */

public class MatrixOperatingView extends View implements ICustomView {
    Bitmap mBitmap, mBitmapBg, mTestBitMap;
    Matrix mMatrix = new Matrix();
    Matrix mMatrixBg = new Matrix();
    Paint mPaint = new Paint();
    int currentTag = 0;
    android.os.Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            currentTag++;
            invalidate();
            return false;
        }
    });

    public MatrixOperatingView(Context context) {
        super(context);
        init();
    }

    public MatrixOperatingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MatrixOperatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBitmap.recycle();
        mBitmapBg.recycle();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        currentTag = 0;
    }

    @Override
    public void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explore_column_day);
        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.test_pic);
        refresh();
    }

    private void refresh() {
        mHandler.sendEmptyMessageDelayed(currentTag, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipRect(0, 0, getWidth(), getHeight() / 3);
        canvas.drawBitmap(mBitmapBg, 0, 0, mPaint);
        mMatrixBg.reset();
        //因为本身图片就比当前画布大，所以这样按中心点缩放是放不到正中间的
        mMatrixBg.postScale(0.3f, 0.3f, getWidth() / 2, getHeight() / 3 / 2);
        canvas.drawBitmap(mBitmapBg, mMatrixBg, mPaint);
        canvas.restore();

        //按画布正中心缩放，此时图片刚刚好就是画布大小
        canvas.save();
        canvas.translate(0, getHeight() / 3 * 2);
        if (null == mTestBitMap) {
            mTestBitMap = Bitmap.createBitmap(mBitmapBg, 0, 0, getWidth(), getHeight() / 3);
        }
        canvas.drawBitmap(mTestBitMap, 0, 0, mPaint);
        mMatrixBg.reset();
        mMatrixBg.postScale(0.3f, 0.3f, getWidth() / 2, getHeight() / 3 / 2);
        canvas.drawBitmap(mTestBitMap, mMatrixBg, mPaint);
        canvas.restore();
        // 看一下四个操作是怎么回事
        if (currentTag == 0) return;
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.drawLine(-getWidth() / 2, 0f, getWidth() / 2, 0f, mPaint);
        canvas.drawLine(0, getHeight() / 6, 0, -getHeight() / 6, mPaint);
        switch (currentTag) {
            case 1:
                translate(canvas);
                refresh();
                break;
            case 2:
                translate(canvas);
                scale(canvas);
                refresh();
                break;
            case 3:
                translate(canvas);
                scale(canvas);
                rotate(canvas);
                refresh();
                break;
            case 4:
                translate(canvas);
                scale(canvas);
                rotate(canvas);
                skew(canvas);
                break;
        }
        canvas.restore();
    }

    private void skew(Canvas canvas) {
        mMatrix.reset();
        mMatrix.preSkew(0.5f, 0.5f);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    private void rotate(Canvas canvas) {
        mMatrix.reset();
        mMatrix.preRotate(120);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    private void scale(Canvas canvas) {
        mMatrix.reset();
        mMatrix.preScale(0.5f, 0.5f);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    private void translate(Canvas canvas) {
        mMatrix.reset();
        mMatrix.preTranslate(100, 100);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
