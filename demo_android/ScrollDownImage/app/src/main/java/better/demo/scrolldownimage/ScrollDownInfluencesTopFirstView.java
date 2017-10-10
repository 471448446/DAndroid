package better.demo.scrolldownimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.os.Build;
import android.widget.Scroller;

/**
 * 第一个元素是顶部的View，展示背景
 * 第二个元素时可以下拉的元素
 * 第三个是顶部的布局
 * <p>
 * 滑动没有到顶部的时，滑动顶部也可以展开
 * Created by better on 2017/9/29 13:46.
 */

public class ScrollDownInfluencesTopFirstView extends ViewGroup implements ViewTreeObserver.OnGlobalLayoutListener {


    public interface OnScrollTopListener {
        boolean isScrolledToTop();
    }

    private OnScrollTopListener mOnScrollTopListener;
    private float downY, actionY;
    private Scroller mScroller;
    private int currentTopH;
    private boolean hasInit;

    private float mMiniProportion = 0.5f, mMaxProportion = 1f;
    private int mBottomMarginTop;

    public void setOnScrollTopListener(OnScrollTopListener onScrollTopListener) {
        mOnScrollTopListener = onScrollTopListener;
    }

    public ScrollDownInfluencesTopFirstView(Context context) {
        super(context);
        init(context, null);
    }

    public ScrollDownInfluencesTopFirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public ScrollDownInfluencesTopFirstView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(getContext());
        if (null != attrs) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScrollDownInfluencesTopFirstView);
            mMiniProportion = array.getFloat(R.styleable.ScrollDownInfluencesTopFirstView_miniProportion, mMiniProportion);
            mMaxProportion = array.getFloat(R.styleable.ScrollDownInfluencesTopFirstView_maxProportion, mMaxProportion);
            mBottomMarginTop = array.getDimensionPixelSize(R.styleable.ScrollDownInfluencesTopFirstView_bottomMarginTop, mBottomMarginTop);
            array.recycle();
        }
        getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    @Override
    public void onGlobalLayout() {
        //作为Fragment的时候，需要再次布局？
        if (hasInit) return;
        hasInit = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if (currentTopH == 0) {
            currentTopH = (int) (getMeasuredWidth() * mMiniProportion);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //不考虑child GONE
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (0 == i) {
                child.layout(l, -Math.abs(child.getMeasuredHeight() - currentTopH), l + child.getMeasuredWidth(), child.getMeasuredHeight());
            } else if (1 == i) {
                child.layout(l, currentTopH - mBottomMarginTop, l + child.getMeasuredWidth(), currentTopH + child.getMeasuredHeight());
            } else {
                child.getLayoutParams().height = currentTopH;
                child.layout(l, t, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //子View没有点击事件
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            return true;
        }
        handleMoveTop(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                actionY = ev.getY();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //下滑
                if (ev.getY() - downY > 0) {
                    if (ev.getY() < getMeasuredWidth() * mMiniProportion ||
                            null != mOnScrollTopListener && mOnScrollTopListener.isScrolledToTop())
                        return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            handleMoveTop(mScroller.getCurrY() - actionY);
            actionY = mScroller.getCurrY();
            invalidate();
        }
    }

    private void handleMoveTop(MotionEvent ev) {
        if (MotionEvent.ACTION_UP == ev.getAction()) {
            mScroller.startScroll(0, (int) ev.getY(), 0, -(int) Math.abs(ev.getY() - downY), 400);
            actionY = ev.getY();
            invalidate();
        } else {
            handleMoveTop(ev.getY() - actionY);
            actionY = ev.getY();
        }
    }

    private void handleMoveTop(float dy) {
        float newH = currentTopH + dy;
        if (newH > getMeasuredWidth() * mMaxProportion) {
            newH = getMeasuredWidth() * mMaxProportion;
        } else if (newH < getMeasuredWidth() * mMiniProportion) {
            newH = getMeasuredWidth() * mMiniProportion;
        }
        currentTopH = (int) newH;
        requestLayout();
    }
}
