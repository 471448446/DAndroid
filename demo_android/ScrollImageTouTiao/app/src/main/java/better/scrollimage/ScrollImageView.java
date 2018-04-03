package better.scrollimage;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 移动内容
 * Created by better on 2018/3/5 14:10.
 */

public class ScrollImageView extends LinearLayout {

    public interface OnDismissImage {
        void onDismissImage();

        void onUIVisibleChange(boolean showOrGone);
    }

    private int colorStart = Color.parseColor("#ff000000");

    private float yDown = 0;
    private Scroller mScroller;
    private int speed = 10 * 300;
    private float alphaSpeed = 2;
    private boolean isFinish;

    private OnDismissImage mOnDismissImage;

    public ScrollImageView setOnDismissImage(OnDismissImage onDismissImage) {
        mOnDismissImage = onDismissImage;
        return this;
    }

    public ScrollImageView(Context context, AttributeSet attr) {
        super(context, attr);
        mScroller = new Scroller(getContext());
        setBackgroundColor(colorStart);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                yDown = ev.getY();
                isFinish = false;
                break;
            case MotionEvent.ACTION_MOVE:
                scrollTo(0, (int) (yDown - ev.getY()));
                changeBackGround(Math.abs(yDown - ev.getY()));
                break;
            case MotionEvent.ACTION_UP:
                int length = (int) (yDown - ev.getY());

                if (getHeight() * 1.0f / 4 < Math.abs(ev.getY() - yDown)) {
                    isFinish = true;
                    int dy;
                    if (ev.getY() > yDown) {
                        dy = -(getHeight() - Math.abs(length));
                    } else {
                        dy = getHeight() - Math.abs(length);
                    }
                    mScroller.startScroll(0, length, 0, dy, 300);
                } else {
                    mScroller.startScroll(0, length,
                            0, -length, length / speed);
                }

                invalidate();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void changeBackGround(float length) {
        float l = length * alphaSpeed > getHeight() ? getHeight() : (length * alphaSpeed);
        float abs = l / getHeight();
        String alpha = Integer.toHexString((int) ((1 - abs) * 255));

        if (abs > 0.05) {
            if (null != mOnDismissImage) {
                mOnDismissImage.onUIVisibleChange(false);
            }
        } else {
            if (null != mOnDismissImage) {
                mOnDismissImage.onUIVisibleChange(true);
            }
        }
        try {
            int color = Color.parseColor("#" + alpha + "000000");
            setBackgroundColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            changeBackGround(Math.abs(mScroller.getCurrY()));
            invalidate();
            if (mScroller.getFinalY() == mScroller.getCurrY() &&
                    isFinish) {
                if (null != mOnDismissImage) {
                    mOnDismissImage.onDismissImage();
                }
            }
        }
    }
}
