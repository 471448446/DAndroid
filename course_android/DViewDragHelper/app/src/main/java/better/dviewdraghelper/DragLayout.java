package better.dviewdraghelper;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by better on 16/8/21.
 * 第一个元素添加了点击事件,就不能移动?
 * processTouchEvent()是在onTouchEvent()中调用的,而走到这里的前提之一是
 * 1子类不消耗某个事件,这样事件就默认传递到onTouchEvent()。
 * 2在Up事件之前或者Up事件onInterceptTouchEvent()返回true,事件传递到onTouchEvent()。
 */
public class DragLayout extends LinearLayout {
    ViewDragHelper mHelper;
    View mDragView, mAutoBackView, mEdgeView;
    int autoX, autoY;

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        //tryCaptureView如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child != mEdgeView;
        }

        //第二个参数是指当前拖动子view应该到达的x坐标
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            l("" + left + ",dx=" + dx);
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - child.getWidth() - leftBound;
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;
//            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int topBound = getPaddingTop();
            int bottomBound = getHeight() - child.getHeight() - topBound;
            return Math.min(Math.max(top, topBound), bottomBound);
//            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (releasedChild == mAutoBackView) {
                mHelper.settleCapturedViewAt(autoX, autoY);
                invalidate();
            }
        }

        //在边界拖动时回调
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mHelper.captureChildView(mEdgeView, pointerId);
        }
    };

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = ViewDragHelper.create(this, 1.0f, callback);
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        autoX = mAutoBackView.getLeft();
        autoY = mAutoBackView.getTop();
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int action= MotionEventCompat.getActionMasked(ev);
//        if (MotionEvent.ACTION_CANCEL==action||MotionEvent.ACTION_UP==action){
//            mHelper.cancel();
//            return false;
//        }
//        mHelper.processTouchEvent(ev);
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action= MotionEventCompat.getActionMasked(ev);
        l("onInterceptTouchEvent()1111,"+action+","+mHelper.shouldInterceptTouchEvent(ev));
        if (MotionEvent.ACTION_CANCEL==action||MotionEvent.ACTION_UP==action){
            mHelper.cancel();
            return false;
        }
//        if (MotionEvent.ACTION_CANCEL==action){
//            mHelper.cancel();
//            return false;
//        }else if (MotionEvent.ACTION_UP==action){
//            mHelper.processTouchEvent(ev);
//        }
        l("onInterceptTouchEvent()222,"+action+","+mHelper.shouldInterceptTouchEvent(ev));
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        super.onTouchEvent(event);
//        final int action = MotionEventCompat.getActionMasked(event);
        mHelper.processTouchEvent(event);
//        if (MotionEvent.ACTION_UP == action) {
//            return super.onTouchEvent(event);
//        } else
        l("onTouchEvent()");
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeView = getChildAt(2);
    }

    private void l(String msg) {
        Log.d("Better", msg);
    }

}
