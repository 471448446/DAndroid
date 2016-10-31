package better.hello.ui.news.newslist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import better.hello.R;
import better.hello.common.RefreshListener;
import better.hello.common.RefreshListenerPresentImpl;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NewsListBean;
import better.hello.ui.base.BaseListFragment;
import better.hello.ui.base.adapter.BaseRecyclerViewAdapter;
import better.hello.ui.news.detail.NewsPhotoDetailActivity;
import better.hello.ui.news.detail.NewsTextDetailsActivity;
import better.hello.util.C;
import better.hello.util.UiHelper;
import better.lib.http.RequestType;

/**
 * Created by better on 2016/10/14.
 */

public class NewsListFragment extends BaseListFragment<NewsListBean> implements NewsListContract.view {
    private NewsListPresenter mPresenter;
    private String type, id;

    public static NewsListFragment newInstance(String type, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(C.EXTRA_TAG_Type, type);
        bundle.putString(C.EXTRA_TAG_ID, id);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initWhenNullRootView() {
        super.initWhenNullRootView();
        mPresenter = new NewsListPresenter(NewsListFragment.this, type, id);
        initRefresh(R.id.simpleRefresh_SwipeRefresh, R.id.simpleRefresh_recyclerView);
        mPresenter.asyncList(RequestType.DATA_REQUEST_INIT);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle b = getArguments();
        if (null != b) {
            type = b.getString(C.EXTRA_TAG_Type);
            id = b.getString(C.EXTRA_TAG_ID);
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
        return new NewsListAdapter(mContext, newsItemClickListener);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.layout_simplerefresh;
    }

    private NewsItemClickListener newsItemClickListener = new NewsItemClickListener() {
        @Override
        public void onClickNews(boolean isPhoto, NewsListBean bean) {
            if (isPhoto){
                NewsPhotoDetailActivity.start(mContext.getActivity(), UiHelper.getImage(bean.getImgextra(),bean.getAds()));
            }else {
                NewsTextDetailsActivity.start(mContext.getActivity(),bean.getPostid());
            }
        }
    };

    private ArrayList<ImagesDetailsBean> getNewsImage(NewsListBean bean) {
        ArrayList<ImagesDetailsBean> list=new ArrayList<>();
        if (null!=bean.getAds()){
            for (NewsListBean.AdsBean im :bean.getAds()){
                list.add(new ImagesDetailsBean(im.getTitle(),im.getImgsrc()));
            }
        }else if (null!=bean.getImgextra()){
            for (NewsListBean.ImgextraBean im :bean.getImgextra()){
                list.add(new ImagesDetailsBean("",im.getImgsrc()));
            }
        }
        return list;
    }

    /**
     * Des todoApp
     * Create By better on 2016/10/19 14:29.
     */
    public interface NewsItemClickListener {
        void onClickNews(boolean isPhoto, NewsListBean bean);
    }
}
