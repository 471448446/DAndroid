package better.dviewdraghelper;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by better on 16/8/22.
 * http://www.flavienlaurent.com/blog/2013/08/28/each-navigation-drawer-hides-a-viewdraghelper/
 * http://githubonepiece.github.io/2015/12/15/YouTube-Demo/
 * 为什么headView点击事件无效?
 * 滑动Head,YouTuBeLayout怎么变矮了,怎样做到缩放的?
 * 点击head怎么变大变小的?
 */
public class YouTuBeLayout extends ViewGroup {
    ViewDragHelper mHelper;
    View mHeaderView, mDescView;
    private int mDragRange;
    private int mTop;
    float mDragOffset;
    private float mInitialMotionX, mInitialMotionY;

    public YouTuBeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = ViewDragHelper.create(this, 1.0f, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderView = findViewById(R.id.viewHeader);
        mDescView = findViewById(R.id.viewDesc);
        l("heard ID="+mHeaderView.getId());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec, 0),
                resolveSizeAndState(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();
        l("onLayout;mDragRange="+mDragRange+",getHeight="+getHeight()+",mHeaderView.getHeight()="+mHeaderView.getHeight());
        mHeaderView.layout(0, mTop, r, mTop + mHeaderView.getMeasuredHeight());
        mDescView.layout(0, mTop + mHeaderView.getMeasuredHeight(), r, mTop + b);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        l("onInterceptTouchEvent+action A=" + action);
        if ((action != MotionEvent.ACTION_DOWN)) {
            mHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        l("onInterceptTouchEvent+action B=" + action);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mHelper.cancel();
            return false;
        }
        l("onInterceptTouchEvent+action C=" + action);
        final float x = ev.getX();
        final float y = ev.getY();
        l("onInterceptTouchEvent+action event=" + x+","+y);
        boolean interceptTap = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                //对于点击heardView始终未true
                //判断(x,y)坐标是否在headView的范围内,注意这个范围不是指可见范围,
                //而是最开始`headView的大小我们动画过程中headView会不断变小,
                //这只是肉眼所见到的可见范围,实际上headView还是和最开始的一样大.
                interceptTap = mHelper.isViewUnder(mHeaderView, (int) x, (int) y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mHelper.getTouchSlop();
                if (ady > slop && adx > ady) {
                    mHelper.cancel();
                    return false;
                }
            }
        }
        //返回true之后,那么事件就不会进入这个事件,代码里可以看出,如果是heardView的点击恒定返回true,
        // 之后的up事件也直接传递到Layout的onTouchEvent()方法,所以heardView就收不到点击事件。
        l("onInterceptTouchEvent+action ------return=" + mHelper.shouldInterceptTouchEvent(ev) + "||" + interceptTap);
        return mHelper.shouldInterceptTouchEvent(ev) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mHelper.processTouchEvent(ev);
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        boolean isHeaderViewUnder = mHelper.isViewUnder(mHeaderView, (int) x, (int) y);
        l("onTouchEvent+action----A=" + action + ",isHeaderViewUnder=" + isHeaderViewUnder);
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mHelper.getTouchSlop();
                //判断滑动的距离小于触发距离,判定为点击并确认是点击headView
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        smoothSlideTo(1f);
                    } else {
                        smoothSlideTo(0f);
                    }
                }
                break;
            }
        }
        l("onTouchEvent+action----return=" + (isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y)) + "||" + isViewHit(mDescView, (int) x, (int) y));
        return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y) || isViewHit(mDescView, (int) x, (int) y);
    }

    boolean smoothSlideTo(float slideOffset) {
        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mDragRange);
        if (mHelper.smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        //tryCaptureView如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHeaderView;
        }

//        @Override
//        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            return left;
//        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mHeaderView.getHeight() - getPaddingBottom();
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            mTop = top;
            mDragOffset = top / mDragRange;
            l("onViewPositionChanged()"+",changedView id="+changedView.getId()+",top="+top+",left="+left+",mDragRange="+mDragRange+",mDragOffset="+mDragOffset);
            mHeaderView.setPivotX(mHeaderView.getWidth());
            mHeaderView.setPivotY(mHeaderView.getHeight());
            mHeaderView.setScaleX(1 - mDragOffset / 2);
            mHeaderView.setScaleY(1 - mDragOffset / 2);
            mDescView.setAlpha(1 - mDragOffset);
            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += mDragRange;
            }
            mHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }
    };

    private void l(String msg) {
        Log.d("Better", "YouTuBeLayout:" + msg);
    }
}
