package better.hello.ui.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import better.hello.R;
import better.hello.data.bean.NewsChannelBean;
import better.hello.http.wait.ProgressWait;
import better.hello.ui.base.BaseTabSlideFragment;
import better.hello.ui.base.adapter.TabPagerAdapter;
import better.hello.ui.news.newslist.NewsListFragment;
import better.hello.util.Utils;
import better.lib.waitpolicy.WaitPolicy;
import butterknife.BindView;

/**
 * Created by better on 2016/10/19.
 */

public class NewsTabFragment extends BaseTabSlideFragment implements NewsTabContract.view {
    @BindView(R.id.simpleTabSlide_lay)
    RelativeLayout mRelativeLayout;
    private String[] name;
    private List<NewsChannelBean> channels;
    private ProgressWait mProgressWait;
    private NewsTabPresenter mPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new NewsTabPresenter(this);
        setPresenterProxy(mPresenter);
        mPresenter.getChannel(false);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        if (null == channels) return null;
        name = new String[channels.size()];
        for (int i = 0, l = channels.size(); i < l; i++) {
            name[i] = channels.get(i).getName();
        }
        if (null == getChildFragmentManager()) Utils.toastShort(getContext(), "tab配置不合法");
        return new TabPagerAdapter(getChildFragmentManager(), name) {
            @Override
            public Fragment getItem(int position) {
                return NewsListFragment.newInstance(channels.get(position));
            }
        };
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_news_tab;
    }

    @Override
    public void setChannel(List<NewsChannelBean> data, boolean updata) {
        this.channels = data;
        if (!updata) {
            initTabSlide((TabLayout) getActivity().findViewById(R.id.main_news_toolBar), (ViewPager) mRootView.findViewById(R.id.simpleTabSlide_Pager));
        } else {
            viewPager.removeAllViews();
            viewPager.setAdapter(getPagerAdapter());
        }
    }

    @Override
    public void upDataChannel() {
        mPresenter.getChannel(true);
    }

    @Override
    public WaitPolicy getWait() {
        if (null == mProgressWait) {
            mProgressWait = new ProgressWait(getContext());
            mRelativeLayout.addView(mProgressWait.getView(), 0, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
        return mProgressWait;
    }
}
