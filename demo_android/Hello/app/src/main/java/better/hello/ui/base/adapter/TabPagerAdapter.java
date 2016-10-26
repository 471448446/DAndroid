package better.hello.ui.base.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Better on 2016/3/15.
 * 为TableLayout创建的Adapter 基类
 */
public abstract class TabPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public TabPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles =titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
