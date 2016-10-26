package better.hello.common;

import better.lib.http.RequestType;

/**
 * Created by better on 2016/10/18.
 * 常用列表刷新操作。
 */

public class RefreshListenerPresentImpl implements RefreshListener {
    private DataSourceAsyncListener mPresenter;
    public RefreshListenerPresentImpl(DataSourceAsyncListener listener){
        this.mPresenter=listener;
        if (null==this.mPresenter) throw new IllegalArgumentException("-------->DataSourceAsyncListener can not null");
    }

    @Override
    public void onRetryListEmptyView() {
        mPresenter.asyncList(RequestType.DATA_REQUEST_INIT);
    }

    @Override
    public void onRetryFootEmptyView() {
        mPresenter.asyncList(RequestType.DATA_REQUEST_UP_REFRESH);
    }

    @Override
    public void onRefreshList() {
        mPresenter.asyncList(RequestType.DATA_REQUEST_DOWN_REFRESH);
    }

    @Override
    public void onBottomList() {
        mPresenter.asyncList(RequestType.DATA_REQUEST_UP_REFRESH);
    }
}
