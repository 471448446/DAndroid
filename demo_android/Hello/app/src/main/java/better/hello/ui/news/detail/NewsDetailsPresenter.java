package better.hello.ui.news.detail;

import java.util.List;

import better.hello.common.BasePresenterProxy;
import better.hello.data.bean.NewsListBean;
import better.hello.data.source.NewsCollectDataSourceImp;
import rx.Subscriber;

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

    public void isCollectThis(String key) {
        mSubscription = mSourceImp.getOne(key).subscribe(new Subscriber<List<NewsListBean>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mView.isCollect(false);
            }

            @Override
            public void onNext(List<NewsListBean> newsListBean) {
                mView.isCollect(null != newsListBean && !newsListBean.isEmpty());
            }
        });
    }
}
