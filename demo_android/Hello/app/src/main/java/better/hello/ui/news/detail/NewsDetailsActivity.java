package better.hello.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.util.List;

import better.hello.R;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.data.bean.NewsListBean;
import better.hello.data.bean.ShareBean;
import better.hello.data.bean.VideoBean;
import better.hello.data.source.NewsCollectDataSourceImp;
import better.hello.ui.base.BaseDetailActivity;
import better.hello.ui.news.newslist.NewsListFragment;
import better.hello.util.C;
import better.lib.utils.ForWord;
import butterknife.BindView;

public class NewsDetailsActivity extends BaseDetailActivity implements NewsDetailsContract.view {
    @BindView(R.id.newsDetail_web)
    WebView mWebView;
    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;

    private NewsListBean mNewsListBean;
    private NewsContentHelper mNewsContentHelper;
    private NewsCollectDataSourceImp mCollectDataSourceImp;
    private NewsDetailsPresenter mPresenter;

    public static void start(Activity activity, NewsListBean newsListBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(C.EXTRA_BEAN, newsListBean);
        ForWord.to(activity, NewsDetailsActivity.class, bundle);
    }

    public static void start(Fragment activity, NewsListBean newsListBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(C.EXTRA_BEAN, newsListBean);
        ForWord.to(activity, NewsDetailsActivity.class, bundle, NewsListFragment.req);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mNewsListBean = getIntent().getExtras().getParcelable(C.EXTRA_BEAN);
        isCollected = null == mNewsListBean ? false : mNewsListBean.isCollect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
    }

    @Override
    protected void initData() {
        super.initData();
        if (null == mNewsListBean) return;
        mContainer = findViewById(R.id.activity_news_details);
        mPresenter = new NewsDetailsPresenter(this);
        setPresenterProxy(mPresenter);
        setBackToolBar(toolbar, ""/*mNewsListBean.getTitle()*/);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.addJavascriptInterface(new ClickJs(), "app");
        /*
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        */
//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                addImageClickListener();
//            }
//        });
        mNewsContentHelper = new NewsContentHelper(this, mNewsListBean.getSourceType(), mNewsListBean.getNewsId(), mWebView);
        mNewsContentHelper.showContent();
    }

    @Override
    protected void onMenuCreated() {
        super.onMenuCreated();
        mPresenter.isCollectThis(mNewsListBean.getTitle());
    }

    /**
     * Des 这个可以  注入javascript方法
     * Create By better on 2016/12/13 16:09.
     */
    private void addImageClickListener() {
        this.mWebView.loadUrl("javascript:(function(){" +
                "    var objs = document.getElementsByTagName(\"img\"); " +
                "    for(var i=0;i<objs.length;i++){    " +
                "    objs[i].onclick=function(){" +
                "          if (!(new RegExp(\"android_asset\").test(this.src)))" +
                "          window.app.openImage(this.src);     " +
                "          }  " +
                "    }" +
                "    var objs = document.getElementsByTagName(\"video\"); " +
                "    for(var i=0;i<objs.length;i++){    " +
                "    objs[i].onclick=function(){     " +
                "          window.app.openVideo(this.src);     " +
                "          }  " +
                "    }" +
                "})()");
    }

    @Override
    protected ShareBean getShareTitle() {
        return (null == mNewsContentHelper || null == mNewsContentHelper.getNewsDetailsBean()) ? null : new ShareBean(mNewsContentHelper.getNewsDetailsBean().getTitle(), mNewsContentHelper.getNewsDetailsBean().getShareLink());
    }

    @Override
    public void deliverBackData(Object... o) {
        super.deliverBackData(o);
        Intent intent = new Intent();
        intent.putExtra(C.EXTRA_FIRST, (String) o[0]);
        intent.putExtra(C.EXTRA_SECOND, (Boolean) o[1]);
        setResult(Activity.RESULT_OK, intent);
    }

    @Override
    protected void addToCollection() {
        if (null == mCollectDataSourceImp) {
            mCollectDataSourceImp = new NewsCollectDataSourceImp(mContext);
        }
        mNewsListBean.setIsCollect(true);
        mPresenter.collect(mNewsListBean);
        deliverBackData(mNewsListBean.getTitle(), true);
    }

    @Override
    protected void removeFromCollection() {
        if (null == mCollectDataSourceImp) {
            mCollectDataSourceImp = new NewsCollectDataSourceImp(mContext);
        }
        mPresenter.delete(mNewsListBean.getTitle());
        deliverBackData(mNewsListBean.getTitle(), false);
    }

    @Override
    public void isCollect(boolean collected) {
        isCollected = collected;
        updateCollectionMenu();
    }

    class ClickJs {
        @JavascriptInterface
        public void openImage(String src) {
            if (src.contains(C.MAP4)) {
                VideoBean bean = null;
                for (NewsDetailsBean.VideoBean b : mNewsContentHelper.getNewsDetailsBean().getVideo()) {
                    if (src.contains(b.getCover())) {
                        String url = b.getMp4Hd_url();
                        if (TextUtils.isEmpty(url)) url = b.getMp4_url();
                        else if (!TextUtils.isEmpty(b.getUrl_mp4())) url = b.getUrl_mp4();
                        else url = b.getUrl_m3u8();
                        bean = new VideoBean(b.getCover(), b.getAlt(), url);
                    }
                }
                if (null == bean) return;
                NewsVideoActivity.start(mContext, bean);
            } else {
                int defaultP = 0;
                List<ImagesDetailsBean> list = mNewsContentHelper.getListImgs();
                for (int i = 0, l = list.size(); i < l; i++) {
                    if (list.get(i).getSrc().equals(src)) {
                        defaultP = i;
                    }
                }
                NewsPhotoDetailActivity.start(mContext, list, defaultP);
            }
        }

//        @JavascriptInterface
//        public void openVideo(String src) {
//            VideoBean bean = null;
//            for (NewsDetailsBean.VideoBean b : mNewsContentHelper.getNewsDetailsBean().getVideo()) {
//                if (src.contains(b.getCover())) {
//                    String url = b.getMp4Hd_url();
//                    if (TextUtils.isEmpty(url)) url = b.getMp4_url();
//                    else if (!TextUtils.isEmpty(b.getUrl_mp4())) url = b.getUrl_mp4();
//                    else url = b.getUrl_m3u8();
//                    bean = new VideoBean(b.getCover(), b.getAlt(), url);
//                }
//            }
//            if (null == bean) return;
//            NewsVideoActivity.start(mContext, bean);
//        }
    }
}
