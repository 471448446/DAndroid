package better.hello.ui.news;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import java.util.List;

import better.hello.data.SourceHelper;
import better.hello.data.bean.NewsChannleBean;
import better.hello.ui.base.SimpleTabSlideFragment;
import better.hello.ui.base.adapter.TabPagerAdapter;
import better.hello.ui.news.newslist.NewsListFragment;

/**
 * Created by better on 2016/10/19.
 */

public class NewsTabFragment extends SimpleTabSlideFragment implements NewsTabContract.view {
    private TabPagerAdapter tabPagerAdapter;
    private String[] name;

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<NewsChannleBean> lis = SourceHelper.getNewsCHannle();
        name = new String[lis.size()];
        for (int i = 0, l = lis.size(); i < l; i++) {
            name[i] = lis.get(i).getName();
        }
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), name) {
            @Override
            public Fragment getItem(int position) {
                return NewsListFragment.newInstace(lis.get(position).getType(), lis
                        .get(position).getChannleId());
            }
        };
        return tabPagerAdapter;
    }
}
