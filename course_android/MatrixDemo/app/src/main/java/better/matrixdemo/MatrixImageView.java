package better.matrixdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

/**
 * https://developer.android.com/reference/android/graphics/Matrix.html?#MPERSP_0
 * http://www.jianshu.com/p/6aa6080373ab
 * http://www.jianshu.com/p/a08e589ce5d4
 * http://www.cnblogs.com/qiengo/archive/2012/06/30/2570874.html
 * Created by better on 2017/4/16 21:05.
 */

public class MatrixImageView extends android.support.v7.widget.AppCompatImageView {
    private Matrix mMatrix = new Matrix();
    private Bitmap mBitmap;

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.matix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        this.mMatrix = matrix;
        super.setImageMatrix(matrix);
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
