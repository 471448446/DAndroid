package better.hello.http.call;

/**
 * Des 网络操作回调
 * Create By better on 2016/10/18 21:07.
 */
public interface RequestCallback<T> {
    /**
     * 请求错误调用
     *
     * @param msg 错误信息
     */
    void onError(RequestInfo<T> requestInfo, String msg);

    /**
     * 请求成功调用
     *
     * @param data 数据
     */
    void onSuccess(RequestInfo<T> requestInfo, T data,Object o);

    /**
     * 请求之前调用
     */
    void onStart(RequestInfo<T> requestInfo);

    /**
     * 请求完成调用
     */
    void onComplete(RequestInfo<T> requestInfo);
}