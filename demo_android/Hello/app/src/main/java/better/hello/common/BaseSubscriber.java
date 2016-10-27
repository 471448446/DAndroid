package better.hello.common;

import android.support.annotation.CallSuper;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import better.hello.App;
import better.hello.http.call.RequestInfo;
import better.hello.util.Utils;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Des 请求各个阶段的展示处理
 * Create By better on 2016/10/17 16:02.
 */
public class BaseSubscriber<T> extends Subscriber<T> {

    private RequestInfo<T> mRequestInfo;
    public BaseSubscriber(RequestInfo<T> info){
        this.mRequestInfo=info;
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        Utils.d("Better","----》onStart BaseSubscriber");
        if (null != mRequestInfo && null != mRequestInfo.getWaitPolicy())
            mRequestInfo.getWaitPolicy().displayLoading();
        if (null != mRequestInfo && mRequestInfo.getRequestCallback() != null) {
            mRequestInfo.getRequestCallback().onStart(mRequestInfo);
        }
    }

    @CallSuper
    @Override
    public void onCompleted() {
        Utils.d("Better","----》onCompleted BaseSubscriber");
        if (null != mRequestInfo && null != mRequestInfo.getWaitPolicy()) {
            mRequestInfo.getWaitPolicy().disappear();
        }
        if (null != mRequestInfo && mRequestInfo.getRequestCallback() != null) {
            mRequestInfo.getRequestCallback().onComplete(mRequestInfo);
        }
    }

    @CallSuper
    @Override
    public void onNext(T t) {
        Utils.d("Better","----》onNext BaseSubscriber");
        if (null != mRequestInfo && mRequestInfo.getRequestCallback() != null) {
            mRequestInfo.getRequestCallback().onSuccess(mRequestInfo, t,"");
        }
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        Utils.d("Better","----》onError BaseSubscriber"+e.getMessage());
        String errorMsg;
        if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case 403:
                    errorMsg = "没有权限访问此链接！";
                    break;
                case 504:
                    if (!Utils.isNetworkAvailable(App.getApplication())) {
                        errorMsg = "没有联网哦！";
                    } else {
                        errorMsg = "网络连接超时！";
                    }
                    break;
                default:
                    errorMsg = ((HttpException) e).message();
                    break;
            }
        } else if (e instanceof UnknownHostException) {
            errorMsg = "不知名主机";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "网络连接超时！";
        }else {
            errorMsg=e.getMessage();
        }
        if (null != mRequestInfo && null != mRequestInfo.getWaitPolicy())
            mRequestInfo.getWaitPolicy().displayRetry(errorMsg);
        if (null != mRequestInfo && mRequestInfo.getRequestCallback() != null)
            mRequestInfo.getRequestCallback().onError(mRequestInfo, errorMsg);
    }

}