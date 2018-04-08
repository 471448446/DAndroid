package better.android.library.empty;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 替换对应的ContentView
 * Created by better on 2018/3/17 22:40.
 */

public class LoadingProxy implements EmptyViewRefreshProxy {

    private View mInitView;
    private ViewGroup mInitParent;
    private int mInitIndex;

    private EmptyViewRefreshProxy mRefreshProxy;

    public LoadingProxy setRefreshProxy(EmptyViewRefreshProxy refreshProxy) {
        mRefreshProxy = refreshProxy;
        return this;
    }

    public LoadingProxy(View view) {

        mRefreshProxy = new EmptyViewProxy(view.getContext());

        mInitView = view;
        if (mInitView.getParent() != null) {
            mInitParent = (ViewGroup) mInitView.getParent();
        } else {
            mInitParent = mInitView.getRootView().findViewById(android.R.id.content);
        }

        for (int i = 0; i < mInitParent.getChildCount(); i++) {
            if (mInitParent.getChildAt(i).getId() == mInitView.getId()) {
                mInitIndex = i;
            }
        }

    }

    private void log(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(this.getClass().getSimpleName(), msg);
        }
    }

    private void removeLoadingView() {
        if (mInitParent.getChildAt(mInitIndex) == mRefreshProxy.getProxyView()) {
            mInitParent.removeViewAt(mInitIndex);
            mInitParent.addView(mInitView, mInitIndex, mInitView.getLayoutParams());
            log("removeLoadingView");
        }
//        for (int i = 0; i < mInitParent.getChildCount(); i++) {
//            if (mInitParent.getChildAt(i) == mRefreshProxy.getProxyView()) {
//                mInitParent.removeViewAt(i);
//                mInitParent.addView(mInitView, i, mInitView.getLayoutParams());
//                log("removeLoadingView");
//            }
//        }

    }

    private void prepareLoadingView() {
        if (mInitParent.getChildAt(mInitIndex) == mInitView) {
            mInitParent.removeViewAt(mInitIndex);
            mInitParent.addView(mRefreshProxy.getProxyView(), mInitIndex, mInitView.getLayoutParams());
        }
    }

    @Override
    public void displayLoading() {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayLoading();
        }
    }

    @Override
    public void displayLoading(int msgId) {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayLoading(msgId);
        }
    }

    @Override
    public void displayLoading(String msg) {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayLoading(msg);
        }
    }

    @Override
    public void displayMessage() {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayMessage();
        }
    }

    @Override
    public void displayMessage(int msg) {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayMessage(msg);
        }
    }

    @Override
    public void displayMessage(String msg) {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayMessage(msg);
        }
    }


    @Override
    public void displayRetry() {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayRetry();
        }
    }

    @Override
    public void displayRetry(int msgId) {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayRetry(msgId);
        }
    }

    @Override
    public void displayRetry(String msg) {
        prepareLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.displayRetry(msg);
        }
    }

    @Override
    public void disappear() {
        removeLoadingView();
        if (null != mRefreshProxy) {
            mRefreshProxy.disappear();
        }
    }

    @Override
    public View getProxyView() {
        if (null != mRefreshProxy) {
            return mRefreshProxy.getProxyView();
        }
        return null;
    }

    @Override
    public void setOnRetryClickListener(OnLrpRetryClickListener listener) {
        if (null != mRefreshProxy) {
            mRefreshProxy.setOnRetryClickListener(listener);
        }
    }
}
