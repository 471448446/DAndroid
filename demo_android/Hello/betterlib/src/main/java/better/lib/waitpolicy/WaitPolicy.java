package better.lib.waitpolicy;

import android.content.Context;

/**
 * Created by Better on 2016/3/14.
 */
public abstract class WaitPolicy {
    protected boolean isToastError,isToastSuccess;
    protected Context mContext;
    ///////////////准备请求
    /**
     * 显示请求等待。
     * @param message 等待提示消息。
     */
    public abstract void displayLoading(String message);
    public abstract void displayLoading();

    ///////////////请求失败
    /**
     * 显示请求重试。
     * @param message 重试提示消息：失败、无数据。
     */
    public abstract void displayRetry(String message);
    public abstract void displayRetry();
    ////////请求成功
    /**
     * 请求页面取消显示
     */
    public abstract void disappear(String msg);
    /**
     * 请求页面取消显示
     */
    public abstract void disappear();
}
