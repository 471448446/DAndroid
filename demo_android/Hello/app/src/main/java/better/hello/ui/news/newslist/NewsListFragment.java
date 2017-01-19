package better.hello.ui.news.newslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import better.hello.R;
import better.hello.common.RefreshListener;
import better.hello.common.RefreshListenerPresentImpl;
import better.hello.data.bean.NewsChannelBean;
import better.hello.data.bean.NewsListBean;
import better.hello.ui.base.BaseListFragment;
import better.hello.ui.base.adapter.BaseRecyclerViewAdapter;
import better.hello.ui.news.detail.NewsDetailsActivity;
import better.hello.ui.news.detail.NewsPhotoDetailActivity;
import better.hello.util.C;
import better.hello.util.RegularUtils;
import better.lib.http.RequestType;

/**
 * Created by better on 2016/10/14.
 */

public class NewsListFragment extends BaseListFragment<NewsListBean> implements NewsListContract.view {
    public static final int req = 100;
    private NewsListPresenter mPresenter;
    private NewsChannelBean mNewsChannelBean;

    public static NewsListFragment newInstance(NewsChannelBean bean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(C.EXTRA_BEAN, bean);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) return;
        if (req == requestCode) {
            ((NewsListAdapterPlus) mAdapter).collect(data.getStringExtra(C.EXTRA_FIRST), data.getBooleanExtra(C.EXTRA_SECOND, false));
        }
    }

    @Override
    protected void initWhenNullRootView() {
        super.initWhenNullRootView();
        mPresenter = new NewsListPresenter(NewsListFragment.this, mNewsChannelBean);
        setPresenterProxy(mPresenter);
        initRefresh(R.id.simpleRefresh_SwipeRefresh, R.id.simpleRefresh_recyclerView);
        mRefreshLayout.setProgressViewOffset(false, 0, (int) (mContext.getResources().getDisplayMetrics().density * 64));
        mPresenter.asyncList(RequestType.DATA_REQUEST_INIT);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle b = getArguments();
        if (null != b) {
            mNewsChannelBean = b.getParcelable(C.EXTRA_BEAN);
        }
    }

    @Override
    protected RefreshListener getRefreshListener() {
        return new RefreshListenerPresentImpl(mPresenter);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return getLinearLayoutManagerV();
    }

    @Override
    protected BaseRecyclerViewAdapter getAdapter() {
        return new NewsListAdapterPlus(mContext, newsItemClickListener);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.layout_simplerefresh;
    }

    private NewsItemClickListener newsItemClickListener = new NewsItemClickListener() {
        @Override
        public void onClickNews(boolean isPhoto, NewsListBean bean) {
//            if (isPhoto) {
//                NewsPhotoDetailActivity.start(mContext.getActivity(), bean.getImgs());
//            } else {
////                NetEasyNewsDetailsActivity.start(mContext.getActivity(),bean.etNewsId());
//                NewsDetailsActivity.start(mContext, bean);
//            }
            if (!TextUtils.isEmpty(bean.getNewsId())&&bean.getNewsId().contains("|")) {
                NewsPhotoDetailActivity.startB(getActivity(), RegularUtils.getImageId(bean.getNewsId()));
            } else {
                NewsDetailsActivity.start(mContext, bean);
            }
        }
    };

    /**
     * Des todoApp
     * Create By better on 2016/10/19 14:29.
     */
    public interface NewsItemClickListener {
        void onClickNews(boolean isPhoto, NewsListBean bean);
    }
}
