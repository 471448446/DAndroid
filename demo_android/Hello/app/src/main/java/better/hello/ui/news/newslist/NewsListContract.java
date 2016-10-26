package better.hello.ui.news.newslist;

import better.hello.common.BasePresenter;
import better.hello.common.BaseView;
import better.hello.common.DataSourceAsyncListener;
import better.hello.common.DataSourceSetListener;
import better.hello.data.bean.NewsListBean;

/**
 * Created by better on 2016/10/18.
 */

public interface NewsListContract {

    interface presenter extends BasePresenter,DataSourceAsyncListener {
//        void asyncList(String type, final String id, int startPage);
    }

    interface view extends BaseView,DataSourceSetListener<NewsListBean> {
//        void showNewsList(List<NewsListBean> list);
    }
}
