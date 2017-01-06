package better.hello.ui.news.detail;

import better.hello.common.BasePresenterProxy;
import better.hello.data.bean.NewsListBean;
import better.hello.data.source.NewsCollectDataSourceImp;

/**
 * Created by better on 2017/1/6.
 */

public class NewsDetailsPresenter extends BasePresenterProxy<NewsDetailsActivity> implements NewsDetailsContract.presenter {
    private NewsCollectDataSourceImp mSourceImp;

    public NewsDetailsPresenter(NewsDetailsActivity mView) {
        super(mView);
        mSourceImp = new NewsCollectDataSourceImp(mView);
    }

    @Override
    public void delete(String key) {
        mSourceImp.delete(key);
    }

    @Override
    public void collect(NewsListBean bean) {
        mSourceImp.collect(bean);
    }
}
