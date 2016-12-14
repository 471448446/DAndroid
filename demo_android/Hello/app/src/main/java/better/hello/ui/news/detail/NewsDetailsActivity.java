package better.hello.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.util.List;

import better.hello.R;
import better.hello.data.bean.ImagesDetailsBean;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.data.bean.NewsListBean;
import better.hello.data.bean.VideoBean;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import butterknife.BindView;

public class NewsDetailsActivity extends BaseActivity {
    @BindView(R.id.newsDetail_web)
    WebView mWebView;
    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;

    private NewsListBean mNewsListBean;
    private NewsContentHelper mNewsContentHelper;

    public static void start(Activity activity, NewsListBean newsListBean) {
        Intent intent = new Intent(activity, NewsDetailsActivity.class);
        intent.putExtra(C.EXTRA_BEAN, newsListBean);
        activity.startActivity(intent);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mNewsListBean = getIntent().getParcelableExtra(C.EXTRA_BEAN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        setBackToolBar(toolbar, mNewsListBean.getTitle());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.addJavascriptInterface(new ClickJs(), "app");
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
