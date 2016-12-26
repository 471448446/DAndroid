package better.hello.ui.aboutme.collect;


import android.support.v7.widget.RecyclerView;

import better.hello.R;
import better.hello.common.RefreshListener;
import better.hello.common.RefreshListenerPresentImpl;
import better.hello.data.bean.NewsListBean;
import better.hello.ui.base.BaseListFragment;
import better.hello.ui.base.adapter.BaseRecyclerViewAdapter;
import better.lib.http.RequestType;

/**
 * Des
 * Create By better on 2016/12/26 13:40.
 */
public class CollectFragment extends BaseListFragment<NewsListBean> implements CollectContract.view {

    private CollectPresenter mCollectPresenter;

    @Override
    protected int getRootViewId() {
        return R.layout.layout_simplerefresh;
    }

    @Override
    protected void initWhenNullRootView() {
        super.initWhenNullRootView();
        mCollectPresenter = new CollectPresenter(this);
        initRefresh(R.id.simpleRefresh_SwipeRefresh, R.id.simpleRefresh_recyclerView);
        mCollectPresenter.asyncList(RequestType.DATA_REQUEST_INIT);
    }

    @Override
    protected RefreshListener getRefreshListener() {
        return new RefreshListenerPresentImpl(mCollectPresenter);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return getLinearLayoutManagerV();
    }

    @Override
    protected BaseRecyclerViewAdapter getAdapter() {
        return new CollectAdapter(getActivity());
    }
}
