package better.hello.ui.news.detail;

import java.util.Map;

import better.hello.common.BasePresenterProxy;
import better.hello.common.BaseSubscriber;
import better.hello.data.bean.NewsDetailsBean;
import better.hello.http.HttpUtil;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.hello.util.HtmlUtil;
import better.lib.waitpolicy.DialogPolicy;
import rx.functions.Func1;

/**
 * Created by better on 2016/10/21.
 */

public class NewsTextDetailsPresenter extends BasePresenterProxy<NewsTextDetailsActivity> implements NewsTextDetailsContract.presenter {

    public NewsTextDetailsPresenter(NewsTextDetailsActivity mView) {
        super(mView);
    }

    @Override
    public void asyncNews(final String posId) {
        HttpUtil.getNewDetail(posId).map(new Func1<Map<String, NewsDetailsBean>, NewsDetailsBean>() {
            @Override
            public NewsDetailsBean call(Map<String, NewsDetailsBean> stringNewsDetailsBeanMap) {
                NewsDetailsBean bean = stringNewsDetailsBeanMap.get(posId);
                bean.setBody(HtmlUtil.createNewsImgTag(bean));
                return bean;
            }
        }).subscribe(new BaseSubscriber<>(new RequestInfo<>(new RequestCallback<NewsDetailsBean>() {
            @Override
            public void onError(RequestInfo<NewsDetailsBean> requestInfo, String msg) {

            }

            @Override
            public void onSuccess(RequestInfo<NewsDetailsBean> requestInfo, NewsDetailsBean data, Object o) {
                mView.showNews(data);
            }

            @Override
            public void onStart(RequestInfo<NewsDetailsBean> requestInfo) {

            }

            @Override
            public void onComplete(RequestInfo<NewsDetailsBean> requestInfo) {
            }
        }, new DialogPolicy(mView.mContext))));
    }
}
