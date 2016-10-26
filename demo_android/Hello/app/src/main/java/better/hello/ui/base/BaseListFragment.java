package better.hello.ui.base;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import better.hello.R;
import better.hello.common.DataSourceSetListener;
import better.hello.common.RefreshListener;
import better.hello.ui.base.adapter.BaseRecyclerViewAdapter;
import better.hello.util.Utils;
import better.lib.http.RequestType;
import better.lib.recyclerview.BRecyclerOnScrollListener;
import better.lib.recyclerview.BRecyclerView;
import better.lib.waitpolicy.emptyproxy.EmptyViewProxy;
import better.lib.waitpolicy.emptyproxy.FooterEmptyView;

/**
 * Created by better on 2016/10/17.
 */

public abstract class BaseListFragment<E> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,DataSourceSetListener<E> {
    @NonNull
    protected BaseRecyclerViewAdapter mAdapter;
    @NonNull
    protected BRecyclerView mRecyclerView;
    @NonNull
    protected SwipeRefreshLayout mRefreshLayout;
    @NonNull
    protected RefreshListener mRefreshListener;
    private boolean isLoadingBottom;

    protected abstract RefreshListener getRefreshListener();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract BaseRecyclerViewAdapter getAdapter();

    protected void initRefresh(int refreshLayoutId, int recyclerId) {
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(refreshLayoutId);
        mRecyclerView = (BRecyclerView) mRootView.findViewById(recyclerId);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mAdapter=getAdapter();
        mRefreshListener =getRefreshListener();

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);

        if (mRecyclerView.isNeedEmptyView()) {
            mRecyclerView.getEmptyViewProxy().setOnRetryClickListener(new EmptyViewProxy.onLrRetryClickListener() {
                @Override
                public void onRetryClick() {
                    mRefreshListener.onRetryListEmptyView();
                }
            }).displayLoading();
        }
        mRecyclerView.setLoadMoreProxy(new FooterEmptyView(getActivity()).setOnRetryClickListener(new EmptyViewProxy.onLrRetryClickListener() {
            @Override
            public void onRetryClick() {
                mRefreshListener.onRetryFootEmptyView();
            }
        }),new BRecyclerOnScrollListener() {
            @Override
            public void onBottom() {
                if (!isLoadingBottom) {
                    isLoadingBottom = true;
                    mRefreshListener.onBottomList();
                }
            }
        });
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mRefreshListener.onRefreshList();
    }
    protected LinearLayoutManager getLinearLayoutManagerV() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }

    @Override
    public void postRequestSuccess(RequestType requestType, List<E> list, String requestMeg) {
        mRefreshLayout.setRefreshing(false);
        boolean isEmpty = null == list||list.isEmpty();
        BaseRecyclerViewAdapter baseAdapter = (BaseRecyclerViewAdapter) mRecyclerView.getWrappedAdapter();
        switch (requestType) {
            case DATA_REQUEST_INIT:
                if (isEmpty && mRecyclerView.isNeedEmptyView())
                    mRecyclerView.getEmptyViewProxy().displayRetry(requestMeg);
                if (null != baseAdapter) baseAdapter.addDownData(list);
                break;
            case DATA_REQUEST_DOWN_REFRESH:
                if (mRecyclerView.isNeedEmptyView())
                    Utils.setGone(mRecyclerView.getEmptyViewProxy().getProxyView());
//                if (isEmpty) Utils.toastShort(getActivity(), R.string.str_loading_header_all);
                if (isEmpty) {
                    if (baseAdapter.getItemCount() > 0)
                        Utils.toastShort(getActivity(), R.string.str_loading_header_all);
                    else if (null != mRecyclerView.getHeadViewProxy())
                        mRecyclerView.getHeadViewProxy().displayMessage(requestMeg);
                }
                if (null != baseAdapter) baseAdapter.addDownData(list);
                break;
            case DATA_REQUEST_UP_REFRESH:
                isLoadingBottom = false;
                if (mRecyclerView.isNeedEmptyView())
                    Utils.setGone(mRecyclerView.getEmptyViewProxy().getProxyView());
                if (isEmpty)
                    if (null != mRecyclerView.getFooterViewProxy())
                        mRecyclerView.getFooterViewProxy().displayMessage(getString(R.string.str_loading_footer_all));
                if (null != baseAdapter) baseAdapter.addPullData(list);
                break;
        }
    }
    @Override
    public void postRequestError(RequestType requestType, ArrayList<E> list, String requestMeg) {
        mRefreshLayout.setRefreshing(false);
        switch (requestType) {
            case DATA_REQUEST_INIT:
                if (mRecyclerView.isNeedEmptyView())
                    mRecyclerView.getEmptyViewProxy().displayRetry(requestMeg);
                break;
            case DATA_REQUEST_DOWN_REFRESH:
                Utils.toastShort(getActivity(), requestMeg);
                break;
            case DATA_REQUEST_UP_REFRESH:
                if (null != mRecyclerView.getFooterViewProxy())
                    mRecyclerView.getFooterViewProxy().displayRetry(requestMeg);
                break;
        }
    }

}
