package better.hello.ui.aboutme.collect;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;
import better.hello.common.DataSourceAsyncListener;
import better.hello.common.DataSourceSetListener;
import better.hello.data.bean.NewsListBean;

/**
 * Created by better on 2016/12/26.
 */

public interface CollectContract {
    interface presenter extends BasePresenter, DataSourceAsyncListener {

    }

    interface view extends BaseView, DataSourceSetListener<NewsListBean> {

    }
}
