package better.hello.ui.news;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

import better.hello.R;
import better.hello.data.SourceHelper;
import better.hello.data.bean.NewsChannelBean;
import better.hello.ui.base.BaseTabSlideFragment;
import better.hello.ui.base.adapter.TabPagerAdapter;
import better.hello.ui.news.newslist.NewsListFragment;
import better.hello.util.Utils;

/**
 * Created by better on 2016/10/19.
 */

public class NewsTabFragment extends BaseTabSlideFragment implements NewsTabContract.view {
    private String[] name;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabSlide((TabLayout) getActivity().findViewById(R.id.main_news_toolBar), (ViewPager) mRootView.findViewById(R.id.simpleTabSlide_Pager));
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<NewsChannelBean> lis = SourceHelper.getNewsChannel();
        name = new String[lis.size()];
        for (int i = 0, l = lis.size(); i < l; i++) {
            name[i] = lis.get(i).getName();
        }
        if (null == getChildFragmentManager()) Utils.toastShort(getContext(), "tab配置不合法");

        return new TabPagerAdapter(getChildFragmentManager(), name) {
            @Override
            public Fragment getItem(int position) {
                return NewsListFragment.newInstance(lis.get(position));
            }
        };
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_news_tab;
    }
}
