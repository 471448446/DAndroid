package better.lib.waitpolicy;

import android.content.Context;

import better.lib.utils.BaseUtils;
import better.lib.waitpolicy.emptyproxy.EmptyViewProxy;

/**
 * Created by Better on 2016/3/14.
 * 默认不提示请求信息
 */
public class EmptyViewWaitPolicy extends WaitPolicy {
    private EmptyViewProxy emptyViewProxy;
    private Context mContext;

    public EmptyViewWaitPolicy(EmptyViewProxy emptyViewProxy) {
        this.emptyViewProxy = emptyViewProxy;
    }
    public EmptyViewWaitPolicy(EmptyViewProxy emptyViewProxy,Context context,boolean isToasterror,boolean isToastSuccess) {
        this.emptyViewProxy = emptyViewProxy;
        this.mContext=context;
        this.isToastError=isToasterror;
        this.isToastSuccess=isToastSuccess;
    }
    @Override
    public void displayLoading(String message) {
        emptyViewProxy.displayLoading(message);
    }

    @Override
    public void displayLoading() {
        emptyViewProxy.displayLoading();
    }

    @Override
    public void displayRetry(String message) {
        emptyViewProxy.displayRetry(message);
        if(isToastError&&null!=mContext)BaseUtils.toastShort(mContext, message);
    }

    @Override
    public void displayRetry() {
        emptyViewProxy.displayRetry();
    }

    @Override
    public void disappear() {
        BaseUtils.setGone(emptyViewProxy.getProxyView());
    }

    @Override
    public void onNext(Object bean) {

    }

    @Override
    public void disappear(String msg) {
        disappear();
        if(isToastSuccess&&null!=mContext) BaseUtils.toastShort(mContext, msg);
    }
}
