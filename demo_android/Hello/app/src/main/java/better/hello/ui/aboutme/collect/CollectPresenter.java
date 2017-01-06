package better.hello.ui.aboutme.collect;


import android.support.v4.util.ArrayMap;

import java.util.List;

import better.hello.AppConfig;
import better.hello.common.BasePresenterProxy;
import better.hello.data.bean.NewsListBean;
import better.hello.data.source.NewsCollectDataSourceImp;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.lib.http.RequestType;
import better.lib.waitpolicy.NoneWaitPolicy;

/**
 * Created by better on 2016/12/26.
 */

public class CollectPresenter extends BasePresenterProxy<CollectFragment> implements CollectContract.presenter, RequestCallback<List<NewsListBean>> {
    private NewsCollectDataSourceImp mCollectDataSourceImp;
    private int mStart = 0;

    public CollectPresenter(CollectFragment mView) {
        super(mView);
        mCollectDataSourceImp = new NewsCollectDataSourceImp(mView.getContext());
    }

    @Override
    public void asyncList(RequestType type) {
        if (RequestType.DATA_REQUEST_DOWN_REFRESH == type) mStart = 0;
        ArrayMap<String, Object> p = new ArrayMap<>();
        p.put(C.EXTRA_FIRST, mStart);
        RequestInfo<List<NewsListBean>> requestInfo = new RequestInfo<>(this, new NoneWaitPolicy(), type, p);
        mSubscription = mCollectDataSourceImp.getLocalInfo(requestInfo);
    }

    @Override
    public void onError(RequestInfo<List<NewsListBean>> requestInfo, String msg) {
        mView.postRequestError(requestInfo.getRequestTye(), null, msg);
    }

    @Override
    public void onSuccess(RequestInfo<List<NewsListBean>> requestInfo, List<NewsListBean> data, Object o) {
        mStart += AppConfig.PAGES_SIZE;
        mView.postRequestSuccess(requestInfo.getRequestTye(), data, "");
    }

    @Override
    public void onStart(RequestInfo<List<NewsListBean>> requestInfo) {

    }

    @Override
    public void onComplete(RequestInfo<List<NewsListBean>> requestInfo) {

    }
    public void delete(String key){
        mCollectDataSourceImp.delete(key);
    }
}
