package better.hello.ui.news.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.List;

import better.hello.R;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import better.hello.util.HtmlUtil;
import better.hello.util.ImageGetter;
import better.hello.util.LinkMovementMethodExt;
import better.hello.util.UiHelper;
import butterknife.BindView;

/**
 * Des 新闻文本信息
 * Create By better on 2016/10/26 10:25.
 */
public class NewsTextDetailsActivity extends BaseActivity implements NewsTextDetailsContract.view {
    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.news_detail_txt)
    TextView detail;
    private NewsTextDetailsPresenter presenter;
    private String key_postId;

    public static void start(Activity activity, String postId) {
        Intent i = new Intent(activity, NewsTextDetailsActivity.class);
        i.putExtra(C.EXTRA_BEAN, postId);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_text_details);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new NewsTextDetailsPresenter(this);
        if (!TextUtils.isEmpty(key_postId)) {
            presenter.asyncNews(key_postId);
        }
        setBackToolBar(toolbar);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        key_postId = getIntent().getStringExtra(C.EXTRA_BEAN);

    }

    @Override
    public void showNews(NewsDetailsBean bean) {
        toolbar.setTitle(bean.getTitle());
        final List<NewsDetailsBean.ImgBean> list = bean.getImg();
        detail.setText(HtmlUtil.from(bean.getBody(), new ImageGetter(detail, list)));
        detail.setMovementMethod(new LinkMovementMethodExt().setOnClickImageSpans(new LinkMovementMethodExt.OnClickImageSpan() {
            @Override
            public void onClick(ImageSpan span) {
                int defaultP = 0;
                for (int i = 0, l = list.size(); i < l; i++) {
                    if (list.get(i).getSrc().equals(span.getSource())) {
                        defaultP = i;
                    }
                }
                NewsPhotoDetailActivity.start(mContext, UiHelper.getImage(list), defaultP);
            }
        }));
    }
}
