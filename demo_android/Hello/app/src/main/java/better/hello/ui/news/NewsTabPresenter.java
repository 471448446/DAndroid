package better.hello.ui.news;


import android.support.v4.util.ArrayMap;

import java.util.List;

import better.hello.common.BasePresenterProxy;
import better.hello.data.bean.NewsChannelBean;
import better.hello.data.source.NewsChannelDataSource;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.Utils;

/**
 * Created by better on 2016/10/19.
 */

public class NewsTabPresenter extends BasePresenterProxy<NewsTabFragment> implements NewsTabContract.presenter {
    private NewsChannelDataSource mNewsChannelDataSource;

    public NewsTabPresenter(NewsTabFragment mView) {
        super(mView);
        mNewsChannelDataSource = new NewsChannelDataSource(mView.getContext());
    }

    public void getChannel(final boolean isUpdata) {
        RequestInfo<List<NewsChannelBean>> requestInfo = new RequestInfo<>(new RequestCallback<List<NewsChannelBean>>() {
            @Override
            public void onError(RequestInfo<List<NewsChannelBean>> requestInfo, String msg) {
                Utils.toastShort(mView.getContext(), msg);
            }

            @Override
            public void onSuccess(RequestInfo<List<NewsChannelBean>> requestInfo, List<NewsChannelBean> data, Object o) {
                mView.setChannel(data,isUpdata);
            }

            @Override
            public void onStart(RequestInfo<List<NewsChannelBean>> requestInfo) {

            }

            @Override
            public void onComplete(RequestInfo<List<NewsChannelBean>> requestInfo) {

            }
        }, mView.getWait());
        ArrayMap<String, Object> p = new ArrayMap<>();
        p.put(C.EXTRA_FIRST, NewsChannelBean.SELECTED);
        requestInfo.setPrams(p);
        mNewsChannelDataSource.getChannel(requestInfo);
    }
}
