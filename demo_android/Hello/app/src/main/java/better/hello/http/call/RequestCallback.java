package better.hello.http.call;

import better.lib.waitpolicy.WaitPolicy;

public interface RequestCallback<T> {
    /**
     * 请求错误调用
     *
     * @param msg 错误信息
     */
    void onError(WaitPolicy waitPolicy,String msg);

    /**
     * 请求成功调用
     *
     * @param data 数据
     */
    void onSuccess(WaitPolicy waitPolicy,T data);
    /**
     * 请求之前调用
     */
    void onStart(WaitPolicy waitPolicy);

    /**
     * 请求完成调用
     */
    void onComplete(WaitPolicy waitPolicy);
}