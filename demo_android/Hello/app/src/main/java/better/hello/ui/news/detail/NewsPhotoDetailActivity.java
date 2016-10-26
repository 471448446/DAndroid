package better.hello.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import better.hello.R;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import butterknife.BindView;

public class NewsPhotoDetailActivity extends BaseActivity {
    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.news_detail_pager)
    ViewPager pager;

    private Adapter mAdapter;
    private List<ImagesDetailsBean> fragmentList;
    private String source;
    private int mDefaultP;

    public static void start(Activity activity, List<ImagesDetailsBean> fragmentList) {
        start(activity, fragmentList, 0);
    }

    public static void start(Activity activity, List<ImagesDetailsBean> fragmentList, int selectP) {
        Intent i = new Intent(activity, NewsPhotoDetailActivity.class);
        i.putExtra(C.EXTRA_BEAN, (Serializable) fragmentList);
        i.putExtra(C.EXTRA_TAG_ID, selectP);
        activity.startActivity(i);
    }

    public static void start(Activity activity, String src) {
        Intent i = new Intent(activity, NewsPhotoDetailActivity.class);
        i.putExtra(C.EXTRA_BEAN, src);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_photo_detail);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        fragmentList = getIntent().getParcelableArrayListExtra(C.EXTRA_BEAN);
        if (null == fragmentList||fragmentList.isEmpty()) {
            source = getIntent().getStringExtra(C.EXTRA_BEAN);
            if (!TextUtils.isEmpty(source)) {
                fragmentList = new ArrayList<>();
                fragmentList.add(new ImagesDetailsBean("", source));
            }
        }
        if (null == fragmentList) fragmentList = new ArrayList<>();
        mDefaultP = getIntent().getIntExtra(C.EXTRA_TAG_ID, 0);
    }

    @Override
    protected void initData() {
        super.initData();
        setBackToolBar(toolbar, R.string.image);
        mAdapter = new Adapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(mAdapter);
        pager.setCurrentItem(mDefaultP);
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    public class Adapter extends FragmentStatePagerAdapter {
        private List<ImagesDetailsBean> mFragmentList;

        public Adapter(FragmentManager fm, List<ImagesDetailsBean> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return PagerImageDetails.getInstance(mFragmentList.get(position).getSrc());
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
