package better.hello.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import better.hello.R;
import better.hello.data.bean.NewsListBean;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import butterknife.BindView;

public class NewsDetailsActivity extends BaseActivity {
    @BindView(R.id.newsDetail_web)
    WebView mWebView;

    private NewsListBean mNewsListBean;

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
    }
}
