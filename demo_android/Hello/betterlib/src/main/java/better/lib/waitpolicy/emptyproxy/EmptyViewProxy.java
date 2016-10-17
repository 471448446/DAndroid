package better.lib.waitpolicy.emptyproxy;

import android.view.View;

/**
 * Created by Better on 2016/3/13.
 */
public abstract class EmptyViewProxy {
    protected onLrRetryClickListener mListener;
    public abstract void displayMessage();
    public abstract void displayMessage(String msg);
    public abstract void displayRetry();
    public abstract void displayRetry(String msg);
    public abstract void displayNone();
    public abstract void displayLoading();
    public abstract void displayLoading(String msg);
    public abstract View getProxyView();
    public EmptyViewProxy setOnRetryClickListener(onLrRetryClickListener listener) {
        mListener = listener;
        return this;
    }
    public interface onLrRetryClickListener {
         void onRetryClick();
    }
}
