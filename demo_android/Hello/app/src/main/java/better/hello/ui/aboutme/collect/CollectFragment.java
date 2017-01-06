package better.hello.ui.aboutme.collect;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import better.hello.AppConfig;
import better.hello.R;
import better.hello.common.LinearItemDecoration;
import better.hello.common.RefreshListener;
import better.hello.common.RefreshListenerPresentImpl;
import better.hello.data.bean.NewsListBean;
import better.hello.ui.base.BaseListFragment;
import better.hello.ui.base.adapter.BaseRecyclerViewAdapter;
import better.lib.http.RequestType;
import better.lib.waitpolicy.emptyproxy.FooterEmptyView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Des
 * Create By better on 2016/12/26 13:40.
 */
public class CollectFragment extends BaseListFragment<NewsListBean> implements CollectContract.view {

    private CollectPresenter mCollectPresenter;
    private Subscription mSubscription;

    @Override
    protected int getRootViewId() {
        return R.layout.layout_simplerefresh;
    }

    @Override
    protected void initWhenNullRootView() {
        super.initWhenNullRootView();
        mCollectPresenter = new CollectPresenter(this);
        initRefresh(R.id.simpleRefresh_SwipeRefresh, R.id.simpleRefresh_recyclerView);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(ContextCompat.getColor(getActivity(), R.color.colorHorizontalDivision)));
        ((FooterEmptyView) mRecyclerView.getFooterViewProxy()).getRetryView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLayBg));
        mCollectPresenter.asyncList(RequestType.DATA_REQUEST_INIT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mSubscription) mSubscription.unsubscribe();
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
        return new CollectAdapter(CollectFragment.this);
    }

    @Override
    public void postRequestSuccess(RequestType requestType, List<NewsListBean> list, String requestMeg) {
        super.postRequestSuccess(requestType, list, requestMeg);
        if (null != list && AppConfig.PAGES_SIZE > list.size())
            if (RequestType.DATA_REQUEST_INIT == requestType) {
                mRecyclerView.getFooterViewProxy().displayMessage(getString(R.string.str_loading_header_all));
            }
    }

    public void delete(String key, final int position) {
        mSubscription = Observable.just(key).doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                mCollectPresenter.delete(s);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mAdapter.reMove(position);
            }
        });
    }
}
