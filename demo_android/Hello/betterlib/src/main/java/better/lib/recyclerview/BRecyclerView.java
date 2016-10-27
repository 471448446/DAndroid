package better.lib.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import better.lib.R;
import better.lib.utils.BaseUtils;
import better.lib.waitpolicy.emptyproxy.DefaultEmptyView;
import better.lib.waitpolicy.emptyproxy.EmptyViewProxy;

/**
 * Created by Better on 2016/3/11.
 * 添加EmptyView,BRecyclerView的刷新控件外层必须的是LinearLayout，否则刷新无效。
 * loadingMore 采用RecyclerView的多布局来实现。
 * xml中设置需要footer
 * thk
 * Recycler添加emptyView http://blog.csdn.net/sbsujjbcy/article/details/46574421
 * http://stackoverflow.com/questions/27414173/equivalent-of-listview-setemptyview-in-recyclerview
 * 刷新RecyclerView，下拉刷新用SwipeRefreshLayout 所以默认支持EmptyView，支持footerView
 * 当一个屏幕占不满时如何 隐藏footerView？？
 */
public class BRecyclerView extends RecyclerView {
    private EmptyViewProxy mEmptyViewProxy;
    private int mDefaultItemCount = 1;//默认RecyclerViewAdapter count 展示emptyView，默认展示FooterView，所以当adapter，count等于1表述没有数据，展示emptyview
    private boolean hasPreparedEmpty,//确保emptyView只添加一次
            isNeedEmptyView = true,//默认是有的
            isNeedFooter = true, isNeedHeader;
    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            adapterItemChanged();
        }
    };
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener;

    private HeaderViewProxyRecyclerAdapter proxyRecyclerAdapter;

    public BRecyclerView(Context context) {
        super(context);
        prepareEmptyView(null);
    }

    public BRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValues(context, attrs);
        prepareEmptyView(null);
    }

    public BRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        prepareEmptyView(null);
    }

    private void initValues(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BRecyclerView);
            final int N = typedArray.getIndexCount();
            for (int i = 0; i < N; i++) {
                initCustomAttr(typedArray.getIndex(i), typedArray);
            }
            typedArray.recycle();
        }
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BRecyclerView_isNeedFooter) {
            isNeedFooter = typedArray.getBoolean(attr, isNeedFooter);
            if (!isNeedFooter) mDefaultItemCount--;//默认已经为其+1了，当不支持footerView时-1
        } else if (attr == R.styleable.BRecyclerView_isNeedHeader) {
            isNeedHeader = typedArray.getBoolean(attr, isNeedHeader);
            if (isNeedHeader) mDefaultItemCount++;
        } else if (attr == R.styleable.BRecyclerView_isNeedEmptyView) {
            isNeedEmptyView = typedArray.getBoolean(attr, isNeedEmptyView);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(prepareProxyAdapter(adapter));
    }

    private HeaderViewProxyRecyclerAdapter prepareProxyAdapter(Adapter adapter) {
        if (null == proxyRecyclerAdapter){
            proxyRecyclerAdapter = new HeaderViewProxyRecyclerAdapter(adapter).setIsShowFooterView(isNeedFooter).setIsShowHeaderView(isNeedHeader);
            proxyRecyclerAdapter.registerAdapterDataObserver(emptyObserver);
        }else {
            proxyRecyclerAdapter.setAdapter(adapter);
        }
        emptyObserver.onChanged();//不一定有用，没有完成布局 获取不到parent
        return proxyRecyclerAdapter;
    }

    public HeaderViewProxyRecyclerAdapter getProxyAdapter() {
        return proxyRecyclerAdapter;
    }

    public Adapter getWrappedAdapter() {
        return proxyRecyclerAdapter.getWrappedAdapter();
    }

    //=======常用Proxy方法=======
    public void setFootViewProxy(EmptyViewProxy emptyViewProxy) {
        proxyRecyclerAdapter.addFooterViewProxy(emptyViewProxy);
    }

    public EmptyViewProxy getFooterViewProxy() {
        return proxyRecyclerAdapter.getFooterViewProxy();
    }

    public void setHeadViewProxy(EmptyViewProxy emptyViewProxy) {
        proxyRecyclerAdapter.addHeadViewProxy(emptyViewProxy);
    }

    public EmptyViewProxy getHeadViewProxy() {
        return proxyRecyclerAdapter.getFooterViewProxy();
    }

    public EmptyViewProxy getEmptyViewProxy() {
        return mEmptyViewProxy;
    }

    public void setLoadMoreProxy(EmptyViewProxy footerEmptyView, OnScrollListener listener) {
        if (isNeedFooter) {
            setFootViewProxy(footerEmptyView);
            BRecyclerView.this.addOnScrollListener(listener);
        }
    }

    public EmptyViewProxy setEmptyViewProxy(EmptyViewProxy proxy) {
        prepareEmptyView(proxy);
        return mEmptyViewProxy;
    }
    //=======常用Proxy方法=======

    private void removeRecyclerEmptyView() {
        try {
            LinearLayout layout = (LinearLayout) BRecyclerView.this.getParent().getParent();
            layout.removeView(mEmptyViewProxy.getProxyView());
        } catch (Exception e) {
            Log.w("BRecyclerView", "parent  不是LinearLayout" + e.getMessage());
        }
    }

    private void addRecyclerEmptyView() {
        try {
            LinearLayout layout = (LinearLayout) BRecyclerView.this.getParent().getParent();
            layout.addView(mEmptyViewProxy.getProxyView(), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//            log(String.valueOf(null == layout) + "," + String.valueOf(null == BRecyclerView.this.getParent()));
            adapterItemChanged();
        } catch (Exception e) {
            Log.w("BRecyclerView", "parent  不是LinearLayout" + e.getMessage());
        }
    }

    /**
     * 准备 emptyView的 parent
     */
    private void prepareEmptyView(EmptyViewProxy proxy) {
        if (!isNeedEmptyView) return;
        if (null == proxy)
            mEmptyViewProxy = new DefaultEmptyView(getContext());
        else {
            removeRecyclerEmptyView();
            mEmptyViewProxy = proxy;
        }
        if (getWidth() == 0) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (!hasPreparedEmpty) {
                        hasPreparedEmpty = true;
                        if (isNeedEmptyView) {
                            addRecyclerEmptyView();
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            BRecyclerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        } else {
            addRecyclerEmptyView();
        }
    }


    private void adapterItemChanged() {
        if (null != proxyRecyclerAdapter && null != mEmptyViewProxy && hasPreparedEmpty) {
            log("adapter count=" + proxyRecyclerAdapter.getItemCount());
            if (mDefaultItemCount == proxyRecyclerAdapter.getItemCount()) {
                BaseUtils.setGone(BRecyclerView.this);
                BaseUtils.setVisible(mEmptyViewProxy.getProxyView());
            } else {
                BaseUtils.setGone(mEmptyViewProxy.getProxyView());
                BaseUtils.setVisible(BRecyclerView.this);
            }
        }
    }

    public boolean isNeedFooter() {
        return isNeedFooter;
    }

    public boolean isNeedHeader() {
        return isNeedHeader;
    }

    public boolean isNeedEmptyView() {
        return isNeedEmptyView;
    }

    private void log(String msg) {
        Log.v("BRecyclerView", msg);
    }
}
