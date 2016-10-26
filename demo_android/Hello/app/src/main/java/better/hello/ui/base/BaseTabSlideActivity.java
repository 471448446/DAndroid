package better.hello.ui.base;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Better on 2016/3/24.
 */
public abstract class BaseTabSlideActivity extends BaseActivity {
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    protected void initTabSlide(int iabId, int pagerId) {
        tabLayout = (TabLayout) findViewById(iabId);
        viewPager = (ViewPager) findViewById(pagerId);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
        reSetTabLayoutMode(tabLayout);
    }

    /**
     * 重写 设置TabLayout的模式
     *
     */
    protected void reSetTabLayoutMode(TabLayout tabLayout) {
    }

    protected abstract PagerAdapter getPagerAdapter();
}
