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
import better.hello.common.UIHelper;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.data.bean.VideoBean;
import better.hello.ui.base.BaseActivity;
import better.hello.util.C;
import better.hello.util.HtmlUtil;
import better.hello.util.ImageGetter;
import better.hello.util.LinkMovementMethodExt;
import butterknife.BindView;

/**
 * Des 新闻文本信息
 * 有问题 https://c.m.163.com/nc/article/C4ABE546000380BQ/full.html
 * 内马尔 BUPAUSEM05298PQT
 * 视频 C4N82B2N000187VE
 * 大图：https://c.m.163.com/nc/article/C5J2OAO90001875O/full.html
 * Create By better on 2016/10/26 10:25.
 */
public class NewsTextDetailsActivity extends BaseActivity implements NewsTextDetailsContract.view {
    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.news_detail_txt)
    TextView detail;
    private NewsTextDetailsPresenter presenter;
    private String key_postId;

    private NewsDetailsBean mBean;

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
//        key_postId = "C4N82B2N000187VE";
    }

    @Override
    public void showNews(NewsDetailsBean bean) {
        mBean = bean;
        toolbar.setTitle(bean.getTitle());
        detail.setText(HtmlUtil.from(bean.getBody(), new ImageGetter(detail, bean.getImg())));
        detail.setMovementMethod(new LinkMovementMethodExt().setOnClickImageSpans(new LinkMovementMethodExt.OnClickImageSpan() {
            @Override
            public void onClick(ImageSpan span) {
                if (span.getSource().contains(".mp4")) {
                    showVideoInfo(span);
                } else {
                    showImageInfo(span);
                }
            }
        }));
    }

    @Override
    public void showImageInfo(ImageSpan span) {
        List<NewsDetailsBean.ImgBean> list = mBean.getImg();
        int defaultP = 0;
        for (int i = 0, l = list.size(); i < l; i++) {
            if (list.get(i).getSrc().equals(span.getSource())) {
                defaultP = i;
            }
        }
        NewsPhotoDetailActivity.start(mContext, UIHelper.getImage(list), defaultP);
    }

    @Override
    public void showVideoInfo(ImageSpan span) {
        VideoBean bean = null;
        for (NewsDetailsBean.VideoBean b : mBean.getVideo()) {
            if (span.getSource().contains(b.getCover())) {
                String url = b.getMp4Hd_url();
                if (TextUtils.isEmpty(url)) url = b.getMp4_url();
                else if (!TextUtils.isEmpty(b.getUrl_mp4())) url = b.getUrl_mp4();
                else url = b.getUrl_m3u8();
                bean = new VideoBean(b.getCover(), b.getAlt(), url);
            }
        }
        if (null == bean) return;
        NewsVideoActivity.start(mContext, bean);
    }
}
