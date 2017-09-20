package better.learn.view.custom.canvas.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import better.learn.view.R;
import better.learn.view.custom.ICustomView;

/**
 * 显示到指定区域rect，图片会被拉升填充
 * 矩阵来变换图片
 * Created by better on 2017/9/19 23:00.
 */

public class BitmapCanvasView extends View implements ICustomView {

    Paint mPaint = new Paint();
    Bitmap resBitmap;
    RectF resPlaceRectF = new RectF();
    Rect resShowRectF = new Rect();
    Matrix mMatrix = new Matrix();

    Picture mPicture1 = new Picture();
    Picture mPicture2 = new Picture();
    Picture mPicture3 = new Picture();

    public BitmapCanvasView(Context context) {
        super(context);
        init();
    }

    public BitmapCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public BitmapCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public void init() {
        resBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.explore_column_day);
        resPlaceRectF.left = resBitmap.getWidth();
        resPlaceRectF.top = resBitmap.getHeight();
        resPlaceRectF.right = resBitmap.getWidth() * 2;
        resPlaceRectF.bottom = resBitmap.getHeight() * 2;
        resShowRectF.right = resBitmap.getWidth() / 2;
        resShowRectF.bottom = resBitmap.getHeight() / 2;

        record();
    }

    private void record() {
        Canvas canvas = mPicture1.beginRecording(500, 500);
        canvas.drawCircle(400, 400, 100, mPaint);
        mPicture1.endRecording();
        Canvas canvas2 = mPicture2.beginRecording(500, 600);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorArc));
        canvas2.drawCircle(400, 500, 100, mPaint);
        mPicture2.endRecording();
        Canvas canvas3 = mPicture3.beginRecording(500, 700);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        canvas3.drawCircle(400, 600, 100, mPaint);
        mPicture3.endRecording();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(resBitmap, 400, 100, mPaint);
        canvas.drawBitmap(resBitmap, resShowRectF, resPlaceRectF, mPaint);

        // set方法是设置变换类型。后面设置的类型会替换前面设置的类型
        // pre方法是保持原有的类型基础之上增加新的类型
        mMatrix.preTranslate(500, 100);
        mMatrix.preRotate(45, resBitmap.getWidth() / 2, resBitmap.getHeight() / 2);
        canvas.drawBitmap(resBitmap, mMatrix, mPaint);

        //1
        mPicture1.draw(canvas);
        //2
        canvas.drawPicture(mPicture2);
        //3
        PictureDrawable pictureDrawable = new PictureDrawable(mPicture3);
        pictureDrawable.setBounds(0, 0, 400, 650);
        pictureDrawable.draw(canvas);
    }
}
