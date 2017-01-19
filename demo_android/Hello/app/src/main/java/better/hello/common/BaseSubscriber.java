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
    private final String TAG="BaseSubscriber";
    private RequestInfo<T> mRequestInfo;
    public BaseSubscriber(RequestInfo<T> info){
        this.mRequestInfo=info;
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        Utils.d(TAG,"BaseSubscriber onStart ----》");
        if (null != mRequestInfo && null != mRequestInfo.getWaitPolicy())
            mRequestInfo.getWaitPolicy().displayLoading();
        if (null != mRequestInfo && mRequestInfo.getRequestCallback() != null) {
            mRequestInfo.getRequestCallback().onStart(mRequestInfo);
        }
    }

    @CallSuper
    @Override
    public void onCompleted() {
        unsubscribe();
        Utils.d(TAG,"BaseSubscriber onCompleted ----》");
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
        Utils.d(TAG,"BaseSubscriber onNext ----》");
        if (null != mRequestInfo && mRequestInfo.getRequestCallback() != null) {
            mRequestInfo.getRequestCallback().onSuccess(mRequestInfo, t,"");
        }
        if (null!=mRequestInfo&&null!=mRequestInfo.getWaitPolicy())mRequestInfo.getWaitPolicy().onNext(t);
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        unsubscribe();
        Utils.d(TAG,"BaseSubscriber onError ----》"+e.getMessage());
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
