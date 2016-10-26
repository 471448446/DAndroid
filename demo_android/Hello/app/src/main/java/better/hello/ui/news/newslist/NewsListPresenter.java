package better.hello.ui.news.newslist;

import java.util.List;
import java.util.Map;

import better.hello.common.BasePresenterProxy;
import better.hello.common.BaseSubscriber;
import better.hello.data.bean.NewsListBean;
import better.hello.http.HttpUtil;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.hello.util.Utils;
import better.lib.http.RequestType;
import better.lib.waitpolicy.NoneWaitPolicy;
import rx.functions.Func1;

/**
 * Created by better on 2016/10/18.
 */

public class NewsListPresenter extends BasePresenterProxy<NewsListFragment> implements NewsListContract.presenter, RequestCallback<List<NewsListBean>> {
    /*请求参数*/
    private int startPage = 0;
    private String mNewsType;
    private String mNewsId;

    public NewsListPresenter(NewsListFragment mView, String mNewsType, String mNewsId) {
        super(mView);
        this.mNewsType = mNewsType;
        this.mNewsId = mNewsId;
    }

    @Override
    public void asyncList(RequestType type) {
        if (RequestType.DATA_REQUEST_INIT == type || RequestType.DATA_REQUEST_DOWN_REFRESH == type) {
            startPage = 0;
        }
        mSubscribe=HttpUtil.getNewsList(mNewsType, mNewsId, startPage).map(new Func1<Map<String, List<NewsListBean>>, List<NewsListBean>>() {
            @Override
            public List<NewsListBean> call(Map<String, List<NewsListBean>> stringListMap) {
                return stringListMap.get(mNewsId);
            }
        }).subscribe(new BaseSubscriber<List<NewsListBean>>(new RequestInfo<>(this, new NoneWaitPolicy(), type)));
    }

    @Override
    public void onError(RequestInfo<List<NewsListBean>> requestInfo, String msg) {
        mView.postRequestError(requestInfo.getRequestTye(), null, msg);
    }

    @Override
    public void onSuccess(RequestInfo<List<NewsListBean>> requestInfo, List<NewsListBean> data, Object o) {
        Utils.d("Better","最终数据"+String.valueOf(null==data));
        mView.postRequestSuccess(requestInfo.getRequestTye(), data, (String) o);
        startPage += 20;
    }

    @Override
    public void onStart(RequestInfo<List<NewsListBean>> requestInfo) {

    }

    @Override
    public void onComplete(RequestInfo<List<NewsListBean>> requestInfo) {

    }
}
