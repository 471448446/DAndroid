package better.hello.http.call;

import better.lib.http.RequestType;
import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2016/10/18.
 * 自定义的网络请求信息，方便回调使用
 */
public class RequestInfo<T> {
    private RequestCallback<T> mRequestCallback;
    private WaitPolicy mWaitPolicy;
    private RequestType mRequestTye;

    public RequestInfo(RequestCallback<T> mRequestCallback, WaitPolicy mWaitPolicy) {
        this.mRequestCallback = mRequestCallback;
        this.mWaitPolicy = mWaitPolicy;
    }

    public RequestInfo(RequestCallback<T> mRequestCallback, WaitPolicy mWaitPolicy, RequestType mRequestTye) {
        this.mRequestCallback = mRequestCallback;
        this.mWaitPolicy = mWaitPolicy;
        this.mRequestTye = mRequestTye;
    }

    public RequestCallback<T> getRequestCallback() {
        return mRequestCallback;
    }

    public void setRequestCallback(RequestCallback<T> mRequestCallback) {
        this.mRequestCallback = mRequestCallback;
    }

    public WaitPolicy getWaitPolicy() {
        return mWaitPolicy;
    }

    public void setWaitPolicy(WaitPolicy mWaitPolicy) {
        this.mWaitPolicy = mWaitPolicy;
    }

    public RequestType getRequestTye() {
        return mRequestTye;
    }

    public void setRequestTye(RequestType mRequestTye) {
        this.mRequestTye = mRequestTye;
    }
}
