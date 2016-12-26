package better.hello.ui.aboutme.collect;


import java.util.List;

import better.hello.common.BasePresenterProxy;
import better.hello.data.bean.NewsListBean;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.lib.http.RequestType;
import better.lib.waitpolicy.NoneWaitPolicy;

/**
 * Created by better on 2016/12/26.
 */

public class CollectPresenter extends BasePresenterProxy<CollectFragment> implements CollectContract.presenter, RequestCallback<List<NewsListBean>> {
    CollectDataSourceImp mCollectDataSourceImp;

    public CollectPresenter(CollectFragment mView) {
        super(mView);
        mCollectDataSourceImp = new CollectDataSourceImp(mView.getContext());
    }

    @Override
    public void asyncList(RequestType type) {
//        ArrayMap<String, Object> p = new ArrayMap<>();
//        p.put(C.EXTRA_FIRST, 15);
        RequestInfo<List<NewsListBean>> requestInfo = new RequestInfo<>(this, new NoneWaitPolicy(), type, null);
        mSubscription = mCollectDataSourceImp.getLocalInfo(requestInfo);
    }

    @Override
    public void onError(RequestInfo<List<NewsListBean>> requestInfo, String msg) {
        mView.postRequestError(requestInfo.getRequestTye(), null, msg);
    }

    @Override
    public void onSuccess(RequestInfo<List<NewsListBean>> requestInfo, List<NewsListBean> data, Object o) {
        mView.postRequestSuccess(requestInfo.getRequestTye(), data, "");
    }

    @Override
    public void onStart(RequestInfo<List<NewsListBean>> requestInfo) {

    }

    @Override
    public void onComplete(RequestInfo<List<NewsListBean>> requestInfo) {

    }
}
