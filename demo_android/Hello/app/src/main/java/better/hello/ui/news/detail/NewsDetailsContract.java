package better.hello.ui.news.detail;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;
import better.hello.data.source.NewsCollect;

/**
 * Created by better on 2017/1/6.
 */

public interface NewsDetailsContract {
    interface presenter extends BasePresenter,NewsCollect {
    }

    interface view extends BaseView {

    }

}
