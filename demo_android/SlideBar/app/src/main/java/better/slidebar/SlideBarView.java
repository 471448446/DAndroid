package better.slidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by better on 16/9/10.
 * 口
 * 口     口
 * --  ---口---
 * 口     口
 * 口
 *
 */
public class SlideBarView extends View implements ICustomView {
    private List<String> mLetters;
    private Paint mPaint/*,mPaintR*/;
    private int mTextSize;
    private int mTextColor;
    private int mTextPressColor;

    private int mHeight;
    private int mWidth;
    private int mADBPadding/*文字上下padding*/;
    private int mCellWidth, cellHeight,/*cell 起始坐标*/
            mCellStartX, mCellStartY;
    private OnScrollListener listener;
    private boolean isPress;

    public SlideBarView(Context context) {
        super(context);
        initDefaultAttr(context);
        initView(context);
    }

    public SlideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }

    public SlideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }

    @Override
    public void initDefaultAttr(Context context) {
        mPaint = new Paint();
        mTextSize = ViewUtil.sp2px(context, 15);
        mTextColor = ContextCompat.getColor(context, android.R.color.black);
        mTextPressColor = mTextColor;
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setAntiAlias(true);
        mADBPadding = ViewUtil.dp2px(context, 4);
//        mPaintR=new Paint();
//        mPaintR.setColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }

    @Override
    public void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideBarView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttrDetail(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    @Override
    public void initCustomAttrDetail(int attr, TypedArray typedArray) {
        if (attr == R.styleable.SlideBarView_slideBarTextSize) {
            mTextSize = typedArray.getDimensionPixelSize(attr, mTextSize);
        } else if (attr == R.styleable.SlideBarView_slideBarTextPressColor) {
            mTextPressColor = typedArray.getColor(attr, mTextPressColor);
        } else if (attr == R.styleable.SlideBarView_slideBarTextColor) {
            mTextColor = typedArray.getColor(attr, mTextColor);
        }
    }

    @Override
    public void initView(Context context) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST ? ViewUtil.dp2px(getContext(), 26) : MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(mWidth), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mLetters) return;
        setPram();
        for (int i = 0, l = mLetters.size(); i < l; i++) {
            int txtY = (int) (mCellStartY + cellHeight * i + cellHeight / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
//            canvas.drawRect((mWidth-mCellWidth)/2,mCellStartY+cellHeight*i,(mWidth+mCellWidth)/2,mCellStartY+cellHeight*(i+1),mPaintR);
//            l("s--s--"+(mCellStartY+cellHeight*i));
//            l("draw,"+mCellStartX+","+txtY+"="+mLetters.get(i));
//            l("s--e--"+(mCellStartY+cellHeight*i+cellHeight));
            if (isPress) {
                mPaint.setColor(mTextPressColor);
            } else {
                mPaint.setColor(mTextColor);
            }
            canvas.drawText(mLetters.get(i), mCellStartX, txtY, mPaint);
        }
    }

    private void setPram() {
        Rect bounds = new Rect();
        int maxTxtH = 0;
        for (int i = 0, l = mLetters.size(); i < l; i++) {
            mPaint.getTextBounds(mLetters.get(i), 0, mLetters.get(i).length(), bounds);
            if (bounds.height() > maxTxtH) {
                maxTxtH = bounds.height();
            }
        }
        cellHeight = maxTxtH + mADBPadding * 2;
        if (cellHeight * mLetters.size() > mHeight) {
            cellHeight = mHeight / mLetters.size();
        }
        mCellWidth = bounds.width();
        mCellStartX = (mWidth - mCellWidth) / 2;
        boolean odd = mLetters.size() % 2 != 0;
        if (odd) {
            mCellStartY = mHeight / 2 - mLetters.size() / 2 * cellHeight - cellHeight / 2;
        } else {
            mCellStartY = mHeight / 2 - mLetters.size() / 2 * cellHeight;
        }
//        l("cell txtH=" + maxTxtH + " cellH=" + cellHeight + "mCellStartY=" + mCellStartY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        l("dis=" + event.getY() + "," + event.getAction());
        float y = event.getY();
        press(true);
        if (mLetters != null && y >= mCellStartY && y <= mCellStartY + mLetters.size() * cellHeight) {
            float dexY = y - mCellStartY;
            int p = (int) dexY / cellHeight;
            if (p == mLetters.size()) {
                //最后一个item会出现
                p--;
            }
//            l(mLetters.get(p));
            if (null != listener) {
                listener.onChoose(p, mLetters.get(p));
                listener.onUp(false);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            press(false);
            if (null != listener) listener.onUp(true);
        }
        return true;
    }

    private void press(boolean isPress) {
        this.isPress = isPress;
        setPressed(isPress);
        invalidate();
    }

    public void setLetters(List<String> abc) {
        this.mLetters = abc;
        invalidate();
    }

    private void l(String msg) {
        Log.d("SlideBarView",msg);
//        Utils.logh("SlideBarView", msg);
    }

    public interface OnScrollListener {
        void onChoose(int p, String letter);

        void onUp(boolean isUp);
    }

    public void setListener(OnScrollListener listener) {
        this.listener = listener;
    }
}
