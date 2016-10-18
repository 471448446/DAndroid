package better.hello.common;

import android.support.annotation.CallSuper;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import better.hello.App;
import better.hello.http.call.RequestCallback;
import better.hello.util.Utils;
import better.lib.waitpolicy.WaitPolicy;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Des 请求各个阶段的展示处理
 * Create By better on 2016/10/17 16:02.
 */
public class BaseSubscriber<T> extends Subscriber<T> {

    private RequestCallback<T> mRequestCallback;
    private WaitPolicy mWaitPolicy;

    public BaseSubscriber(RequestCallback<T> requestCallback) {
        mRequestCallback = requestCallback;
    }

    public BaseSubscriber(RequestCallback<T> requestCallback, WaitPolicy wait) {
        mRequestCallback = requestCallback;
        this.mWaitPolicy = wait;
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        if (null != mWaitPolicy) mWaitPolicy.displayLoading();
        if (mRequestCallback != null) {
            mRequestCallback.onStart(mWaitPolicy);
        }
    }

    @CallSuper
    @Override
    public void onCompleted() {
        if (null != mWaitPolicy) {
            mWaitPolicy.disappear();
        }
        if (mRequestCallback != null) {
            mRequestCallback.onComplete(mWaitPolicy);
        }
    }

    @CallSuper
    @Override
    public void onNext(T t) {
        if (mRequestCallback != null) {
            mRequestCallback.onSuccess(mWaitPolicy, t);
        }
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        String errorMsg = null;
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
        }
        if (null != mWaitPolicy)
            mWaitPolicy.displayRetry(errorMsg);
        if (mRequestCallback != null)
            mRequestCallback.onError(mWaitPolicy, errorMsg);
    }

}
