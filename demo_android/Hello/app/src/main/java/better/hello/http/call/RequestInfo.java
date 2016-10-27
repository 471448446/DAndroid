package better.hello.http.call;


import android.support.v4.util.ArrayMap;

import better.lib.http.RequestType;
import better.lib.waitpolicy.WaitPolicy;

/**
 * Created by better on 2016/10/18.
 * 自定义的请求信息，方便回调使用。
 */
public class RequestInfo<T> {
    private RequestCallback<T> mRequestCallback;
    private WaitPolicy mWaitPolicy;
    private RequestType mRequestTye;
    private ArrayMap<String, Object> mPrams;

    public RequestInfo(RequestCallback<T> mRequestCallback) {
        this(mRequestCallback, null);
    }

    public RequestInfo(RequestCallback<T> mRequestCallback, WaitPolicy mWaitPolicy) {
        this(mRequestCallback, mWaitPolicy, null);
    }

    public RequestInfo(RequestCallback<T> mRequestCallback, WaitPolicy mWaitPolicy, RequestType mRequestTye) {
        this(mRequestCallback, mWaitPolicy, mRequestTye, null);
    }

    public RequestInfo(RequestCallback<T> mRequestCallback, WaitPolicy mWaitPolicy, RequestType mRequestTye, ArrayMap<String, Object> prams) {
        this.mRequestCallback = mRequestCallback;
        this.mWaitPolicy = mWaitPolicy;
        this.mRequestTye = mRequestTye;
        this.mPrams = prams;
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

    public ArrayMap<String, Object> getPrams() {
        return mPrams;
    }

    public void setPrams(ArrayMap<String, Object> prams) {
        this.mPrams = prams;
    }
}
