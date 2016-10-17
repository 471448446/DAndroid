package better.lib.waitpolicy;

import android.content.Context;

import better.lib.utils.BaseUtils;

/**
 * 没有请求提示，静默请求。
 * 默认 没有请求提示
 */
public class NoneWaitPolicy extends WaitPolicy {

    /**
     * 不提示
     */
    public NoneWaitPolicy() {
    }

    public NoneWaitPolicy(Context context, boolean isToastError, boolean isToastSuccess) {
        this.mContext = context;
        this.isToastError = isToastError;
        this.isToastSuccess = isToastSuccess;
    }

    @Override
    public void displayLoading(String message) {

    }

    @Override
    public void displayLoading() {

    }

    @Override
    public void displayRetry(String message) {
        if (isToastError && null != mContext) BaseUtils.toastShort(mContext, message);
    }

    @Override
    public void displayRetry() {

    }

    @Override
    public void disappear() {
    }

    @Override
    public void disappear(String msg) {
        disappear();
        if (isToastSuccess && null != mContext) BaseUtils.toastShort(mContext, msg);
    }
}
