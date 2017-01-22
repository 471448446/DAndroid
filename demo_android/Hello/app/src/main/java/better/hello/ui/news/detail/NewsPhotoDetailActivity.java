package better.hello.ui.news.detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import better.hello.R;
import better.hello.common.UIHelper;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NetEasyImgBean;
import better.hello.http.wait.ProgressWait;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import better.hello.util.Utils;
import better.lib.utils.ForWord;
import better.lib.waitpolicy.WaitPolicy;
import butterknife.BindView;

public class NewsPhotoDetailActivity extends BaseActivity implements NewsPhotoDetailContract.view {
    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.news_detail_pager)
    ViewPager pager;
    @BindView(R.id.activity_news_photo_detail)
    LinearLayout mLayout;
    private Toolbar mToolbar;

    private Adapter mAdapter;
    private NewsPhotoDetailPresenter mPresenter;
    /* 可以直接展示的--新闻详情点击过来的  --better 2017/1/12 10:30. */
    private List<ImagesDetailsBean> fragmentList;
    private String postId;
    private int mDefaultP;

    public static void start(Activity activity, List<ImagesDetailsBean> fragmentList) {
        start(activity, fragmentList, 0);
    }

    public static void start(Activity activity, List<ImagesDetailsBean> fragmentList, int selectP) {
        Bundle b = new Bundle();
        b.putSerializable(C.EXTRA_BEAN, (Serializable) fragmentList);
        b.putInt(C.EXTRA_TAG_ID, selectP);
        ForWord.to(activity, NewsPhotoDetailActivity.class, b);
    }

    public static void start(Activity activity, String src) {
        List<ImagesDetailsBean> fragmentList = new ArrayList<>();
        fragmentList.add(new ImagesDetailsBean("", src));
        start(activity, fragmentList, 0);
    }

    public static void startB(Activity activity, String src) {
        startB(activity, src, null);
    }

    public static void startB(Activity activity, String src, ArrayList<ImagesDetailsBean> fragmentList) {
        Bundle b = new Bundle();
        b.putString(C.EXTRA_SOURCE_TYPE, src);
        if (null != fragmentList) {
            b.putSerializable(C.EXTRA_BEAN, fragmentList);
        }
        ForWord.to(activity, NewsPhotoDetailActivity.class, b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_photo_detail);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        fragmentList = (List<ImagesDetailsBean>) getIntent().getSerializableExtra(C.EXTRA_BEAN);
        if (null == fragmentList) fragmentList = new ArrayList<>();
        mDefaultP = getIntent().getIntExtra(C.EXTRA_TAG_ID, 0);
        postId = getIntent().getStringExtra(C.EXTRA_SOURCE_TYPE);
    }

    @Override
    protected void initData() {
        super.initData();
        mToolbar = setBackToolBar(toolbar, R.string.image);
        mToolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBlack));
        mToolbar.setTitleTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        if (!TextUtils.isEmpty(postId)) {
            mPresenter = new NewsPhotoDetailPresenter(this);
            setPresenterProxy(mPresenter);
            mPresenter.asyncPhoto(postId);
        } else {
            setAdapter(fragmentList);
        }
    }

    private void setAdapter(List<ImagesDetailsBean> fragmentList) {
        mAdapter = new Adapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(mAdapter);
        pager.setCurrentItem(mDefaultP);
    }

    public void showToolBar(boolean isSHow) {
        if (isSHow) {
            Utils.setVisible(toolbar);
        } else {
            Utils.setInvisible(toolbar);
        }
    }

    @Override
    public void showPhoto(NetEasyImgBean bean) {
        setAdapter(UIHelper.prase(bean));
    }

    @Override
    public void showPhotoError(String error) {
        if (null != fragmentList && !fragmentList.isEmpty()) {
            setAdapter(fragmentList);
        } else {
            toast(error);
        }
    }

    @Override
    public WaitPolicy getWait() {
        ProgressWait progressWait = new ProgressWait(mContext);
        mLayout.addView(progressWait.getView(), 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return progressWait;
    }

    public class Adapter extends FragmentStatePagerAdapter {
        private List<ImagesDetailsBean> mFragmentList;

        public Adapter(FragmentManager fm, List<ImagesDetailsBean> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailsFragment.getInstance(mFragmentList.get(position), position + 1, mFragmentList.size());
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
