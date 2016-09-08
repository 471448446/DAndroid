package better.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by better on 16/8/25.
 * 子item水平居中放置
 */
public class FlowTagLayout extends ViewGroup implements ICustomView {
    //    /*选择方式*/
//    public static final int CHOOSE_SINGLE=1;
//    public static final int CHOOSE_MULTI=1;
//    /*对齐方式*/
    public static final int FIT_LEFT = 0;
    public static final int FIT_CENTER = FIT_LEFT + 1;
    public static final int FIT_RIGHT = FIT_CENTER + 1;
    //标签数据
    private AdapterDataSetObserver mObserver;
    private BaseAdapter mAdapter;
    private int mTagLayoutWidth;
    private int mItemLayoutHeight;

    private int mGravity = FIT_CENTER;
    private int mMaxRow=Integer.MAX_VALUE;

    public FlowTagLayout(Context context) {
        super(context);
        initDefaultAttr(context);
        initView(context);
    }

    public FlowTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }

    public FlowTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttr(context);
        initCustomAttr(context, attrs);
        initView(context);
    }

    @Override
    public void initDefaultAttr(Context context) {

    }

    @Override
    public void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowTagLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttrDetail(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    @Override
    public void initCustomAttrDetail(int attr, TypedArray typedArray) {
        if (attr == R.styleable.FlowTagLayout_tagGravity) {
            mGravity = typedArray.getInteger(attr, mGravity);
        }else if (attr==R.styleable.FlowTagLayout_tagMaxRow){
            mMaxRow=typedArray.getInteger(attr,mMaxRow);
        }
    }

    @Override
    public void initView(Context context) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int xPadding = getPaddingLeft() + getPaddingRight();
        int yPadding = getPaddingBottom() + getPaddingTop();
        mTagLayoutWidth = MeasureSpec.getSize(widthMeasureSpec) - xPadding;
        if (null != mAdapter) {
            removeAllViews();
            //tag不能大于一行
            for (int i = 0, l = mAdapter.getCount(); i < l; i++) {
                View childView = mAdapter.getView(i, null, this);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                //用最大的height
                if (childView.getMeasuredHeight() > mItemLayoutHeight)
                    mItemLayoutHeight = childView.getMeasuredHeight();
                LinearLayout currentLay = getCurrentLayout();
                //添加数据或者lay
                if (childView.getMeasuredWidth() <= getItemLayoutLeftWidth(getCurrentLayout())) {
                    l("未满");
                    addTag(widthMeasureSpec, heightMeasureSpec, childView, currentLay);
                } else {
                    if (getChildCount()==mMaxRow){
                        break;
                    }
                    l("换行");
                    addTag(widthMeasureSpec, heightMeasureSpec, childView, null);
                }
            }
        }

        setMeasuredDimension(mTagLayoutWidth + xPadding, getChildCount() * mItemLayoutHeight + yPadding);
//        if (MeasureSpec.AT_MOST == MeasureSpec.getMode(heightMeasureSpec))
//            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), getChildCount() * mItemLayoutHeight + yPadding);
//        else
//            setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (0 == getChildCount()) return;
        int x = 0 + getPaddingLeft();
        int y = 0 + getPaddingTop();
        for (int i = 0, length = getChildCount(); i < length; i++) {
            View child = getChildAt(i);
            child.layout(x, y + i * mItemLayoutHeight, x + child.getMeasuredWidth(), y + (i + 1) * mItemLayoutHeight);
        }
    }

    private void addTag(int widthMeasureSpec, int heightMeasureSpec, View childView, LinearLayout currentLay) {
        if (null==currentLay){
            currentLay = getItemLayout();
            addView(currentLay);
        }
        currentLay.addView(childView);
        l("添加");
        measureChild(currentLay, widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Des 返回最后一个layout进行add
     * Create By better on 16/8/25 15:02.
     */
    private LinearLayout getCurrentLayout() {
        if (0 == getChildCount())
            addView(getItemLayout());
        return (LinearLayout) getChildAt(getChildCount() - 1);
    }

    private int getItemLayoutLeftWidth(LinearLayout currentLay) {
        int useW = 0, l = currentLay.getChildCount();
        for (int i = 0; i < l; i++) {
            View v = currentLay.getChildAt(i);
            MarginLayoutParams p = (MarginLayoutParams) v.getLayoutParams();
            useW += v.getMeasuredWidth() + p.leftMargin + p.rightMargin;
//            l("getItemLayoutLeftWidth,marginL" + p.leftMargin);
        }
        return mTagLayoutWidth - useW;
    }


    private LinearLayout getItemLayout() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(getGravity());
        return layout;
    }

    private int getGravity() {
        if (mGravity == FIT_LEFT) {
            return Gravity.START;
        } else if (mGravity == FIT_CENTER) {
            return Gravity.CENTER_HORIZONTAL;
        } else {
            return Gravity.END;
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
            Toast.makeText(getContext(),"ddd",Toast.LENGTH_SHORT).show();
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
