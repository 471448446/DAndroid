package better.hello.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import better.hello.R;
import better.hello.ui.base.BaseActivity;
import better.hello.ui.news.NewsTabFragment;
import butterknife.BindView;

/**
 * Des http://stackoverflow.com/questions/36060883/how-to-implement-bottom-navigation-tab-as-per-the-google-new-guideline
 * 主页仿知乎，新闻是网页数据
 * Create By better on 2016/10/12 15:03.
 */
public class MainActivity extends BaseActivity implements MainContract.view {
    @BindView(R.id.main_bottomNav)
    BottomNavigationView bottomNavigationView;
    private MainPresenter mainPresenter;
    private BottomItems mBottomItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
        super.initData();
        mainPresenter = new MainPresenter(this);
        mBottomItem = new BottomItems(R.id.main_Content, bottomNavigationView, getSupportFragmentManager());
        mBottomItem.put(R.id.tab_favorites, new NewsTabFragment());
        mBottomItem.showFragment(R.id.tab_favorites);
    }

    static class BottomItems {
        private int containerId;
        private BottomNavigationView bottomNavigationView;
        private FragmentManager manager;
        private Map<Integer, Fragment> items = new HashMap<>();

        public BottomItems(int containerId, BottomNavigationView bottomNavigationView, FragmentManager manager) {
            this.containerId = containerId;
            this.bottomNavigationView = bottomNavigationView;
            this.manager = manager;
            this.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    for (Integer i : items.keySet()) {
                        if (i == item.getItemId()) {
                            showFragment(i);
                        }
                    }
                    return false;
                }
            });
        }

        public void showFragment(Integer i) {
            if (!items.containsKey(i)) return;
            FragmentTransaction tr = manager.beginTransaction();
            if (null != manager.findFragmentByTag(String.valueOf(i))) {
                tr.replace(containerId, items.get(i), String.valueOf(i));
            } else {
                tr.replace(containerId, items.get(i), String.valueOf(i));
                tr.addToBackStack(String.valueOf(i));
            }
            tr.commitAllowingStateLoss();
        }

        public void put(int id, Fragment fragment) {
            items.put(id, fragment);
        }
    }
}
