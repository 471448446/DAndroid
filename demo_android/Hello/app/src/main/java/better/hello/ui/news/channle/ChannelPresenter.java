package better.hello.ui.news.channle;

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
 * Created by better on 2017/1/20.
 */

public class ChannelPresenter extends BasePresenterProxy<ChannelActivity> implements ChannelContract.presenter {
    private NewsChannelDataSource mDataSource;

    public ChannelPresenter(ChannelActivity mView) {
        super(mView);
        mDataSource = new NewsChannelDataSource(mView);
    }

    @Override
    public void showTopChannel() {
        get(NewsChannelBean.SELECTED);
    }

    @Override
    public void showBottomChannel() {
        get(NewsChannelBean.UN_SELECT);
    }

    @Override
    public void save(List<NewsChannelBean> list) {
        mDataSource.save(list);
    }

    @Override
    public void preSave() {
        mDataSource.preSave();
    }

    private void get(final int isTop) {
        RequestInfo<List<NewsChannelBean>> requestInfo = new RequestInfo<>(new RequestCallback<List<NewsChannelBean>>() {
            @Override
            public void onError(RequestInfo<List<NewsChannelBean>> requestInfo, String msg) {
                Utils.toastShort(mView, msg);
            }

            @Override
            public void onSuccess(RequestInfo<List<NewsChannelBean>> requestInfo, List<NewsChannelBean> data, Object o) {
                if (NewsChannelBean.isSelect(isTop)) {
                    mView.showTopChannel(data);
                } else {
                    mView.showBottomChannel(data);
                }
            }

            @Override
            public void onStart(RequestInfo<List<NewsChannelBean>> requestInfo) {

            }

            @Override
            public void onComplete(RequestInfo<List<NewsChannelBean>> requestInfo) {

            }
        }, mView.getWait());
        ArrayMap<String, Object> p = new ArrayMap<>();
        p.put(C.EXTRA_FIRST, isTop);
        requestInfo.setPrams(p);
        mSubscriptions.add(mDataSource.getChannel(requestInfo));
    }
}
