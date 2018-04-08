package better.android.library.empty;

import android.view.View;

public interface EmptyViewRefreshProxy {

    void displayLoading();

    void displayLoading(int msgId);

    /**
     * 显示请求等待。
     *
     * @param msg 等待提示消息。
     */
    void displayLoading(String msg);

    void displayMessage();

    void displayMessage(int msg);

    /**
     * 请求成功 当列表为空的时候展示的信息
     * Create By better on 2017/4/2 07:14.
     */
    void displayMessage(String msg);

    void displayRetry();

    void displayRetry(int msgId);

    /**
     * 请求失败 显示请求重试。
     *
     * @param msg 重试提示消息：失败、无数据。
     */
    void displayRetry(String msg);

    /**
     * 直接消失emptyView
     */

    void disappear();

    View getProxyView();

    void setOnRetryClickListener(OnLrpRetryClickListener listener);
}
