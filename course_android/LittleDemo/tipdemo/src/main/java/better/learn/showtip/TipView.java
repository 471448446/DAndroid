package better.learn.showtip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 1、竖直居中将扣出来的bitmap放置到背景中
 * 2、图片缩放保持padding
 * Created by better on 2018/1/7 16:51.
 */

public class TipView extends FrameLayout {
    //图片起始的水平位置
    public static final int LEFT = 1, CENTER = LEFT + 1, RIGHT = CENTER + 1;
    private TipBean mTipBean;

    private Paint mPaint;

    private int mGravity = CENTER;
    private int mLeft, mTop;

    private int yOffSet;

    public void setTipBean(TipBean tipBean) {
        mTipBean = tipBean;
        invalidate();
        ViewGroup.LayoutParams p = getLayoutParams();
        if (p instanceof MarginLayoutParams) {
            ((MarginLayoutParams) p).topMargin = mTipBean.topMargin + yOffSet;
            setLayoutParams(p);
        }
    }

    public TipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(false);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TipView);
        yOffSet = typedArray.getDimensionPixelSize(R.styleable.TipView_tipViewYOffSet, yOffSet);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mTipBean) {
            return;
        }

        if (mGravity == LEFT) {
            mLeft = getWidth() - getPaddingLeft();
        } else if (mGravity == RIGHT) {
            if (getAvailableW() > mTipBean.bitmap.getWidth()) {
                mLeft = getWidth() - getPaddingRight() - mTipBean.bitmap.getWidth();
            }
        } else {
            if (getAvailableW() > mTipBean.bitmap.getWidth()) {
                mLeft = (int) ((getWidth() - mTipBean.bitmap.getWidth()) * 1.0f / 2);
            }
        }

        //check
        if (0 == mLeft) {
            mLeft = getWidth() - getPaddingLeft();
        }

        if (getAvailableH() > mTipBean.bitmap.getHeight()) {
            mTop = (int) ((getHeight() - mTipBean.bitmap.getHeight()) * 1.0f / 2);
        } else {
            mTop = getHeight() - getPaddingTop();
        }
        canvas.drawBitmap(mTipBean.bitmap, mLeft, mTop, mPaint);
    }

    private int getAvailableW() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getAvailableH() {
        return getHeight() - getPaddingTop() - getPaddingRight();
    }
}
