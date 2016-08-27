package better.library;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by better on 16/8/25.
 * 子item水平居中放置
 */
public class FlowTagLayout extends ViewGroup {
//    /*选择方式*/
//    public static final int CHOOSE_SINGLE=1;
//    public static final int CHOOSE_MULTI=1;
//    /*对齐方式*/
//    public static final int FIT_LEFT=1;
//    public static final int FIT_CENTER=1;
    //标签数据
    private AdapterDataSetObserver mObserver;
    private BaseAdapter mAdapter;
    private int mTagLayoutWidth;
    private int mItemLayoutHeight;

    public FlowTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int xPadding=getPaddingLeft()+getPaddingRight();
        int yPadding=getPaddingBottom()+getPaddingTop();
        mTagLayoutWidth = MeasureSpec.getSize(widthMeasureSpec)-xPadding;
        if (null != mAdapter) {
            removeAllViews();
            for (int i = 0, l = mAdapter.getCount(); i < l; i++) {
                View childView = mAdapter.getView(i, null, this);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                //用最大的height
                if (childView.getMeasuredHeight() > mItemLayoutHeight)
                    mItemLayoutHeight = childView.getMeasuredHeight();
                LinearLayout currentLay = getCurrentLayout(widthMeasureSpec, heightMeasureSpec);
                //添加数据或者lay
                if (childView.getMeasuredWidth() <= getLayoutLeftWidth(currentLay)) {
                    addTag(widthMeasureSpec, heightMeasureSpec, childView, currentLay);
//                    l("currentLay=" + currentLay.getMeasuredHeight() + "," + currentLay.getMeasuredWidth());
                } else {
                    //tag不能大于一行
                    LinearLayout linear = getItemLayout(widthMeasureSpec, heightMeasureSpec);
                    addTag(widthMeasureSpec, heightMeasureSpec, childView, linear);
                    addView(linear);
                }
//                l("for item w=" + childView.getMeasuredWidth() + "," + mItemLayoutHeight + "layout count=" + getChildCount());
            }
        }

        if (MeasureSpec.AT_MOST == MeasureSpec.getMode(heightMeasureSpec))
            setMeasuredDimension(mTagLayoutWidth+xPadding, getChildCount() * mItemLayoutHeight+yPadding);
        else
            setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    private void addTag(int widthMeasureSpec, int heightMeasureSpec, View childView, LinearLayout currentLay) {
        currentLay.addView(childView);
        measureChild(currentLay, widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Des 返回最后一个layout进行add
     * Create By better on 16/8/25 15:02.
     */
    private LinearLayout getCurrentLayout(int widthMeasureSpec, int heightMeasureSpec) {
        if (0 == getChildCount())
            addView(getItemLayout(widthMeasureSpec, heightMeasureSpec));
        return (LinearLayout) getChildAt(getChildCount() - 1);
    }

    private int getLayoutLeftWidth(LinearLayout currentLay) {
        int useW = 0, l = currentLay.getChildCount();
        for (int i = 0; i < l; i++) {
            View v = currentLay.getChildAt(i);
            MarginLayoutParams p = (MarginLayoutParams) v.getLayoutParams();
            useW += v.getMeasuredWidth() + p.leftMargin + p.rightMargin;
//            l("getLayoutLeftWidth,marginL" + p.leftMargin);
        }
        return mTagLayoutWidth - useW;
    }


    private LinearLayout getItemLayout(int widthMeasureSpec, int heightMeasureSpec) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
//        measureChild(layout, widthMeasureSpec, heightMeasureSpec);
        return layout;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (0 == getChildCount()) return;
        int x=0+getPaddingLeft();
        int y=0+getPaddingTop();
        for (int i = 0, length = getChildCount(); i < length; i++) {
            View child = getChildAt(i);
            child.layout(x,y+ i * mItemLayoutHeight, x + child.getMeasuredWidth(), y+ (i + 1) * mItemLayoutHeight);
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        if (null != mAdapter && null != mObserver) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        removeAllViews();
        this.mAdapter = adapter;
        if (mAdapter != null) {
            if (null == mObserver) mObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mObserver);
        }
    }

    public BaseAdapter getAdapter() {
        return this.mAdapter;
    }

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }

    private void l(String msg) {
        Log.d("TagLayout", msg);
    }
}
