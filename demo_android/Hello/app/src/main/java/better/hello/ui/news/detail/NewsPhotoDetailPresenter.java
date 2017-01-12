package better.hello.ui.news.detail;

import better.hello.common.BasePresenterProxy;
import better.hello.common.BaseSubscriber;
import better.hello.data.bean.NetEasyImgBean;
import better.hello.http.HttpUtil;
import better.hello.http.call.RequestCallback;
import better.hello.http.call.RequestInfo;

/**
 * Des 图片
 * Create By better on 2017/1/12 10:36.
 */
public class NewsPhotoDetailPresenter extends BasePresenterProxy<NewsPhotoDetailActivity> implements NewsPhotoDetailContract.presenter {

    public NewsPhotoDetailPresenter(NewsPhotoDetailActivity mView) {
        super(mView);
    }

    @Override
    public void asyncPhoto(String id) {
        mSubscription = HttpUtil.getNewImgDetails(id).subscribe(new BaseSubscriber<>(new RequestInfo<>(new RequestCallback<NetEasyImgBean>() {
            @Override
            public void onError(RequestInfo<NetEasyImgBean> requestInfo, String msg) {
                mView.showPhotoError(msg);

            }

            @Override
            public void onSuccess(RequestInfo<NetEasyImgBean> requestInfo, NetEasyImgBean data, Object o) {
                mView.showPhoto(data);

            }

            @Override
            public void onStart(RequestInfo<NetEasyImgBean> requestInfo) {

            }

            @Override
            public void onComplete(RequestInfo<NetEasyImgBean> requestInfo) {

            }
        }, mView.getWait())));
    }
}
