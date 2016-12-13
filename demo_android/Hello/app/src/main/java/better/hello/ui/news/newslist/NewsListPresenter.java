package better.hello.ui.news.newslist;

import android.support.v4.util.ArrayMap;

import java.util.List;

import better.hello.common.BasePresenterProxy;
import better.hello.data.bean.NewsChannelBean;
import better.hello.data.bean.NewsListBean;
import better.hello.http.api.NewsSourceType;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;
import better.hello.util.C;
import better.hello.util.Utils;
import better.lib.http.RequestType;
import better.lib.waitpolicy.NoneWaitPolicy;

/**
 * 参数是通过构造方法传递过来，也可以通过调用的方式传递pram
 * Created by better on 2016/10/18.
 */

public class NewsListPresenter extends BasePresenterProxy<NewsListFragment> implements NewsListContract.presenter, RequestCallback<List<NewsListBean>> {
    /*请求参数*/
    private int startPage = 0;
    private NewsChannelBean mChannelBean;

    private RequestInfo requestInfo;
    private NewsListDataSourceImpl newsDataSource;

//    public NewsListPresenter(NewsListFragment mView, String mNewsType, String mNewsId) {
//        super(mView);
//        this.mNewsType = mNewsType;
//        this.mNewsId = mNewsId;
//        newsDataSource = new NewsListDataSourceImpl(mView.getContext());
//    }

    public NewsListPresenter(NewsListFragment mView, NewsChannelBean channelBean) {
        super(mView);
        mChannelBean = channelBean;
        newsDataSource = new NewsListDataSourceImpl(mView.getContext());
    }

    @Override
    public void asyncList(RequestType type) {
        if (null == requestInfo) requestInfo = new RequestInfo(this, new NoneWaitPolicy(), type);
        requestInfo.setRequestTye(type);
        ArrayMap<String, Object> map = new ArrayMap<>();
        map.put(C.EXTRA_SOURCE_TYPE, mChannelBean.getSourceType());

        if (NewsSourceType.NETEASE == mChannelBean.getSourceType()) {
            if (RequestType.DATA_REQUEST_INIT == type || RequestType.DATA_REQUEST_DOWN_REFRESH == type) {
                startPage = 0;
            }
            map.put(C.EXTRA_FIRST, mChannelBean.getNetEaseChannel().getType());
            map.put(C.EXTRA_SECOND, mChannelBean.getNetEaseChannel().getChannelId());
            map.put(C.EXTRA_THIRD, startPage);
        }
        requestInfo.setPrams(map);
        mSubscription = newsDataSource.get(requestInfo);

//        mSubscription= HttpUtil.getNewsList(mNewsType, mNewsId, startPage).map(new Func1<Map<String, List<NetEaseNewsListBean>>, List<NetEaseNewsListBean>>() {
//            @Override
//            public List<NetEaseNewsListBean> call(Map<String, List<NetEaseNewsListBean>> stringListMap) {
//                return stringListMap.get(mNewsId);
//            }
//        }).subscribe(new BaseSubscriber<>(requestInfo));
    }

    @Override
    public void onError(RequestInfo<List<NewsListBean>> requestInfo, String msg) {
//        Utils.d("Better", "error type=" + requestInfo.getRequestTye() + ",msg=" + msg);
        mSubscription.unsubscribe();
        if (newsDataSource.isNeedLoadFromNet()) {
            newsDataSource.asyncUrlInfo(requestInfo);
        } else {
            mView.postRequestError(requestInfo.getRequestTye(), null, msg);
        }
    }

    @Override
    public void onSuccess(RequestInfo<List<NewsListBean>> requestInfo, List<NewsListBean> data, Object o) {
        Utils.d("Better", "最终数据" + String.valueOf(null == data));
        //保存数据前先关闭绑定，不然save 里面Transaction会一直回调这个方法，没找到原因的。
        mSubscription.unsubscribe();
        startPage += 20;
//        newsDataSource.save(data, requestInfo);
        mView.postRequestSuccess(requestInfo.getRequestTye(), data, (String) o);
    }

    @Override
    public void onStart(RequestInfo<List<NewsListBean>> requestInfo) {

    }

    @Override
    public void onComplete(RequestInfo<List<NewsListBean>> requestInfo) {

    }
}
