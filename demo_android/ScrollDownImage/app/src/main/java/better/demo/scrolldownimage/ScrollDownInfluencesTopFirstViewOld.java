package better.demo.scrolldownimage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by better on 2017/9/29 13:46.
 */

public class ScrollDownInfluencesTopFirstViewOld extends RelativeLayout {
    public interface OnScrollTopListener {
        boolean isScrolledToTop();

        void onTopMove(float dy);
    }

    private static final String TAG = "better";

    private OnScrollTopListener mOnScrollTopListener;
    private View mTopInfluencesView;
    private float downY, actionY;
    private Scroller mScroller;

    public void setOnScrollTopListener(OnScrollTopListener onScrollTopListener) {
        mOnScrollTopListener = onScrollTopListener;
    }

    public void setTopInfluencesView(View topInfluencesView) {
        mTopInfluencesView = topInfluencesView;
        mTopInfluencesView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleMoveTop(event);
                return false;
            }
        });
    }

    public ScrollDownInfluencesTopFirstViewOld(Context context) {
        super(context);
        init();
    }


    public ScrollDownInfluencesTopFirstViewOld(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ScrollDownInfluencesTopFirstViewOld(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleMoveTop(event);
        return super.onTouchEvent(event);
    }

    private void handleMoveTop(MotionEvent ev) {
        if (MotionEvent.ACTION_UP == ev.getAction()) {
            Log.d(TAG, "handleMoveTop: 开始滑动" + downY + "," + ev.getY());
            mScroller.startScroll(0, (int) ev.getY(), 0, -(int) Math.abs(ev.getY() - downY), 400);
            actionY = ev.getY();
            invalidate();
        } else {
            handleMoveTop(ev.getY() - actionY);
            actionY = ev.getY();
        }
    }

    private void handleMoveTop(float dy) {
        if (null != mOnScrollTopListener) {
            mOnScrollTopListener.onTopMove(dy);
        }
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
                if (null != mOnScrollTopListener && mOnScrollTopListener.isScrolledToTop()) {
                    if (ev.getY() - downY > 0) {
                        Log.d("better", "下拉");
                        return true;
                    }
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
}
