package better.hello.ui.news.detail;

import android.text.style.ImageSpan;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;
import better.hello.data.bean.NewsDetailsBean;

/**
 * Created by better on 2016/10/21.
 */

public interface NewsTextDetailsContract {
    interface presenter extends BasePresenter{
        void asyncNews(String posId);
    }
    interface view extends BaseView{
        void showNews(NewsDetailsBean bean);
        void showImageInfo(ImageSpan span);
        void showVideoInfo(ImageSpan span);
    }
}
