package better.hello.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.MenuItem;
import android.view.View;

import better.hello.R;
import better.hello.ui.aboutme.AboutMeFragment;
import better.hello.ui.base.BaseActivity;
import better.hello.ui.news.NewsTabFragment;
import better.hello.ui.news.channle.ChannelActivity;
import better.hello.util.C;
import better.hello.util.Utils;
import better.lib.utils.ForWord;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Des http://stackoverflow.com/questions/36060883/how-to-implement-bottom-navigation-tab-as-per-the-google-new-guideline
 * 主页仿知乎，新闻是网页数据
 * Create By better on 2016/10/12 15:03.
 */
public class MainActivity extends BaseActivity implements MainContract.view, BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.main_bottomNav)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_appBar)
    AppBarLayout mAppBarLayout;
    private final int CODE_CHANNEL = 10;

    private MainPresenter mainPresenter;
    private BottomItems mBottomItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        UIHelper.downLoad(mContext, FileUtils.getVideoFileDir(this)+ File.separator+"yy"+ ".apk","http://wdj-uc1-apk.wdjcdn.com/0/7c/64897f147d519fc4173c989868a1a7c0.apk");
//        UIHelper.downLoad(new RequestInfo<DownloadingInfo>(new NotificationWaitPolice()), FileUtils.getVideoFileDir(this)+ File.separator+"yy"+ ".apk","http://wdj-uc1-apk.wdjcdn.com/0/7c/64897f147d519fc4173c989868a1a7c0.apk");
//        UIHelper.downLoad(new RequestInfo<DownloadingInfo>(new NotificationWaitPolice()),FileUtils.getVideoFileDir(this)+ File.separator+"ok"+ FileUtils.MAP_4,"http://flv2.bn.netease.com/videolib3/1610/31/GRiHf1453/SD/GRiHf1453-mobile.mp4");
    }

    @Override
    protected void initData() {
        super.initData();
        mainPresenter = new MainPresenter(this);
        mBottomItem = new BottomItems(R.id.main_Content, bottomNavigationView, getSupportFragmentManager()).setListener(this);
        mBottomItem.put(R.id.tab_favorites, new NewsTabFragment());
        mBottomItem.put(R.id.tab_friends, new AboutMeFragment());
        mBottomItem.showFragment(R.id.tab_favorites);
//        mainPresenter.asyncPlashImage();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tab_favorites) {
            Utils.setVisible(mAppBarLayout);
        } else {
            Utils.setGone(mAppBarLayout);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CHANNEL) {
            if (null != mBottomItem.get(R.id.tab_favorites)) {
                int p = 0;
                if (null != data) p = data.getIntExtra(C.EXTRA_FIRST, 0);
                ((NewsTabFragment) mBottomItem.get(R.id.tab_favorites)).upDataChannel(p);
            }
        }
    }

    @OnClick({R.id.main_news_channel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_news_channel:
                ForWord.to(mContext, ChannelActivity.class, CODE_CHANNEL);
                break;
        }
    }

    static class BottomItems {
        private int containerId;
        private BottomNavigationView bottomNavigationView;
        private FragmentManager manager;
        private ArrayMap<Integer, Fragment> items = new ArrayMap<>();
        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

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
                    if (null != mOnNavigationItemSelectedListener)
                        mOnNavigationItemSelectedListener.onNavigationItemSelected(item);
                    return false;
                }
            });
        }

        public BottomItems setListener(BottomNavigationView.OnNavigationItemSelectedListener listener) {
            this.mOnNavigationItemSelectedListener = listener;
            return this;
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

        public Fragment get(int id) {
            return items.get(id);
        }

    }
}
