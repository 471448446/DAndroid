package better.hello.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import better.hello.App;
import better.hello.util.Utils;
import butterknife.ButterKnife;

/**
 * Created by better on 2016/10/17.
 */

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected Fragment mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=this;
        getArgs();
        if (null == mRootView) {
            mRootView = inflater.inflate(getRootViewId(),container,false);
            ButterKnife.bind(this, mRootView);
            initWhenNullRootView();
        }
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        if (null!=refWatcher)refWatcher.watch(this);
    }

    protected abstract int getRootViewId();

    protected void initWhenNullRootView() {
    }
    /**
     * 参数传递
     */
    protected void getArgs() {
    }

    protected void log(String msg) {
        Utils.v(this.getClass().getSimpleName(), msg);
    }


}
